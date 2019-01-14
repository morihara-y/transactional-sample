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

import com.github.morihara.transactional.sample.config.GoodsReceiptServiceTestConfig;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { 
        GoodsReceiptServiceTestConfig.class
})
@Slf4j
public class GoodsReceiptServiceImplTest {
    @Autowired
    private GoodsReceiptDao goodsReceiptDao;

    @Autowired
    @Qualifier("goodsReceiptServiceForRollback")
    private GoodsReceiptService goodsReceiptServiceForRollback;
    
    @Autowired
    @Qualifier("goodsReceiptServiceForCommit")
    private GoodsReceiptService goodsReceiptServiceForCommit;

    @Test
    public void registerNewGoodsReceipt_rollbackTest() {
        List<GoodsReceiptTrnDto> expectedReceipts = goodsReceiptDao.getAll();
        try {
            goodsReceiptServiceForRollback.registerNewGoodsReceipt("GOODS_RECEIPT", "rollback_test", BigDecimal.TEN);
        } catch (RuntimeException e) {
            log.info("Catch exception");
        }
        List<GoodsReceiptTrnDto> actualReceipts = goodsReceiptDao.getAll();
        assertEqualsReceipt(actualReceipts, expectedReceipts);
    }
    
    @Test
    public void registerNewGoodsReceipt_commitTest() {
        List<GoodsReceiptTrnDto> expectedReceipts = goodsReceiptDao.getAll();
        int expectedReceiptCount = expectedReceipts.size() + 1;
        try {
            goodsReceiptServiceForCommit.registerNewGoodsReceipt("GOODS_RECEIPT", "commit_test", BigDecimal.TEN);
        } catch (RuntimeException e) {
            log.error("Catch exception", e);
            fail();
        }
        List<GoodsReceiptTrnDto> actualReceipts = goodsReceiptDao.getAll();
        
        assertThat(actualReceipts.size(), is(expectedReceiptCount));
    }

    private void assertEqualsReceipt(List<GoodsReceiptTrnDto> actual,
            List<GoodsReceiptTrnDto> expected) {
        log.info("actual size: {}, expected size: {}", actual.size(), expected.size());
        if (actual.size() != expected.size()) {
            assertThat(actual.size(), is(expected.size()));
        }
        for (GoodsReceiptTrnDto expectedDto : expected) {
            boolean result = false;
            for (GoodsReceiptTrnDto actualDto : actual) {
                if (actualDto.getGoodsReceiptTrnId().equals(expectedDto.getGoodsReceiptTrnId())
                        && actualDto.getQuantity().compareTo(expectedDto.getQuantity()) == 0) {
                    result = true;
                    break;
                }
            }
            assertTrue(result);
        }
    }
}
