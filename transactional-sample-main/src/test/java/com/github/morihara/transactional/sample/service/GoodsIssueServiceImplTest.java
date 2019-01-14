package com.github.morihara.transactional.sample.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.morihara.transactional.sample.config.GoodsIssueServiceTestConfig;
import com.github.morihara.transactional.sample.dao.GoodsIssueDao;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { 
        GoodsIssueServiceTestConfig.class
})
@Slf4j
public class GoodsIssueServiceImplTest {
    @Autowired
    private GoodsIssueDao goodsIssueDao;

    @Autowired
    @Qualifier("goodsIssueServiceForRollback")
    private GoodsIssueService goodsIssueServiceForRollback;
    
    @Autowired
    @Qualifier("goodsIssueServiceForCommit")
    private GoodsIssueService goodsIssueServiceForCommit;

    @Test
    public void registerNewGoodsIssue_rollbackTest() {
        List<GoodsIssueTrnDto> expectedIssues = goodsIssueDao.getAll();
        try {
            goodsIssueServiceForRollback.registerNewGoodsIssue("GOODS_RECEIPT", "rollback_test", BigDecimal.TEN);
        } catch (TransactionalRuntimeException e) {
            log.info("Catch exception");
        }
        List<GoodsIssueTrnDto> actualIssues = goodsIssueDao.getAll();
        assertEqualsIssue(actualIssues, expectedIssues);
    }
    
    @Test
    public void registerNewGoodsIssue_commitTest() {
        List<GoodsIssueTrnDto> expectedIssues = goodsIssueDao.getAll();
        int expectedIssueCount = expectedIssues.size() + 1;
        try {
            goodsIssueServiceForCommit.registerNewGoodsIssue("GOODS_RECEIPT", "commit_test", BigDecimal.TEN);
        } catch (TransactionalRuntimeException e) {
            log.error("Catch exception", e);
            fail();
        }
        List<GoodsIssueTrnDto> actualIssues = goodsIssueDao.getAll();
        
        assertThat(actualIssues.size(), is(expectedIssueCount));
    }

    private void assertEqualsIssue(List<GoodsIssueTrnDto> actual,
            List<GoodsIssueTrnDto> expected) {
        log.info("actual size: {}, expected size: {}", actual.size(), expected.size());
        if (actual.size() != expected.size()) {
            assertThat(actual.size(), is(expected.size()));
        }
        for (GoodsIssueTrnDto expectedDto : expected) {
            boolean result = false;
            for (GoodsIssueTrnDto actualDto : actual) {
                if (actualDto.getGoodsIssueTrnId().equals(expectedDto.getGoodsIssueTrnId())
                        && actualDto.getQuantity().compareTo(expectedDto.getQuantity()) == 0) {
                    result = true;
                    break;
                }
            }
            assertTrue(result);
        }
    }
}
