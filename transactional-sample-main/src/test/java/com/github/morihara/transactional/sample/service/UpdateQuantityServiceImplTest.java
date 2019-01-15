package com.github.morihara.transactional.sample.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.morihara.transactional.sample.config.UpdateQuantityServiceTestConfig;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.dto.QuantityTrnDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        UpdateQuantityServiceTestConfig.class
})
@Slf4j
public class UpdateQuantityServiceImplTest {
    @Autowired
    private QuantityDao quantityDao;

    @Autowired
    @Qualifier("updateQuantityServiceForCommit")
    private UpdateQuantityService updateQuantityServiceForCommit;

    @Autowired
    @Qualifier("updateQuantityServiceForRollback")
    private UpdateQuantityService updateQuantityServiceForRollback;

    private static final String TEST_QUANTITY_CODE = "junit_test";
    private static final QuantityTrnDto BASE_QUANTITY = QuantityTrnDto.builder()
            .quantityCode(TEST_QUANTITY_CODE)
            .quantity(BigDecimal.TEN)
            .build();

    @Before
    public void setup() {
        quantityDao.remove(TEST_QUANTITY_CODE);
        quantityDao.insert(BASE_QUANTITY);
    }

    @Test
    public void increaseQuantity_rollbackTest() {
        GoodsReceiptTrnDto receiptDto = GoodsReceiptTrnDto.builder()
                .goodsReceiptTrnId(UUID.randomUUID())
                .quantity(BigDecimal.TEN)
                .build();
        try {
            updateQuantityServiceForRollback.increaseQuantity("GOODS_RECEIPT", TEST_QUANTITY_CODE, receiptDto);
        } catch (RuntimeException e) {
            log.info("Catch Exception");
        }
        Optional<QuantityTrnDto> actual = quantityDao.getQuantity(TEST_QUANTITY_CODE);
        if (!actual.isPresent()) {
            log.error("quantity_trn_dto is gone");
            fail();
        }
        assertThat(actual.get().getQuantity(), is(BASE_QUANTITY.getQuantity()));
    }

    @Test
    public void increaseQuantity_commitTest() {
        BigDecimal additionalQuantity = BigDecimal.TEN;
        GoodsReceiptTrnDto receiptDto = GoodsReceiptTrnDto.builder()
                .goodsReceiptTrnId(UUID.randomUUID())
                .quantity(additionalQuantity)
                .build();
        BigDecimal expectedQuantity = BASE_QUANTITY.getQuantity().add(additionalQuantity);
        try {
            updateQuantityServiceForCommit.increaseQuantity("GOODS_RECEIPT", TEST_QUANTITY_CODE, receiptDto);
        } catch (RuntimeException e) {
            log.error("Catch Exception", e);
            fail();
        }
        Optional<QuantityTrnDto> actual = quantityDao.getQuantity(TEST_QUANTITY_CODE);
        if (!actual.isPresent()) {
            log.error("quantity_trn_dto is gone");
            fail();
        }
        assertThat(actual.get().getQuantity(), is(expectedQuantity));
    }
}
