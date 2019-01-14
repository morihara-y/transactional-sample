package com.github.morihara.transactional.sample.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.morihara.transactional.sample.config.GoodsReceiptServiceTestConfig;
import com.github.morihara.transactional.sample.config.JDBCTestConfig;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { 
        JDBCTestConfig.class,
        GoodsReceiptServiceTestConfig.class
})
@Slf4j
public class GoodsReceiptServiceImplTest {
    @Autowired
    private GoodsReceiptDao goodsReceiptDao;
    @Autowired
    private GoodsReceiptService mockedGoodsReceiptService;

    @Test
    public void registerNewGoodsReceipt_rollbackTest() {
        List<GoodsReceiptTrnDto> expectedReceipts = goodsReceiptDao.getAll();
        try {
            mockedGoodsReceiptService.registerNewGoodsReceipt("GOODS_RECEIPT", "rollback_test", BigDecimal.TEN);
        } catch (RuntimeException e) {
            log.info("Catch exception: {}", e);
        }
        List<GoodsReceiptTrnDto> actualReceipts = goodsReceiptDao.getAll();
        assertEqualsReceipt(actualReceipts, expectedReceipts);
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
