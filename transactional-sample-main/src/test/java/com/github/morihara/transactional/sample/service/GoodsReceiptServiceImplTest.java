package com.github.morihara.transactional.sample.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDaoImpl;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
public class GoodsReceiptServiceImplTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void registerNewGoodsReceipt_rollbackTest() {
        GoodsReceiptDao receiptDao = new GoodsReceiptDaoImpl(jdbc);
        UpdateQuantityService updateQuantityServiceMock = mock(UpdateQuantityService.class);
        doThrow(new TransactionalRuntimeException()).when(updateQuantityServiceMock)
                .increaseQuantity(anyString(), anyString(), any(GoodsReceiptTrnDto.class));
        GoodsReceiptService service =
                new GoodsReceiptServiceImpl(receiptDao, updateQuantityServiceMock);

        List<GoodsReceiptTrnDto> expectedReceipts = receiptDao.getAll();

        service.registerNewGoodsReceipt("GOODS_RECEIPT", "rollback_test", BigDecimal.TEN);

        List<GoodsReceiptTrnDto> actualReceipts = receiptDao.getAll();

        assertEqualsReceipt(actualReceipts, expectedReceipts);
    }

    private void assertEqualsReceipt(List<GoodsReceiptTrnDto> actual,
            List<GoodsReceiptTrnDto> expected) {
        for (GoodsReceiptTrnDto expectedDto : expected) {
            boolean result = false;
            for (GoodsReceiptTrnDto actualDto : actual) {
                if (actualDto.getGoodsReceiptTrnId() == expectedDto.getGoodsReceiptTrnId()
                        && actualDto.getQuantity().compareTo(expectedDto.getQuantity()) == 0) {
                    result = true;
                }
            }
            assertTrue(result);
        }
    }
}
