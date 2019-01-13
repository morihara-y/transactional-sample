package com.github.morihara.transactional.sample.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsReceiptServiceImpl implements GoodsReceiptService {
    private final GoodsReceiptDao receiptDao;
    private final UpdateQuantityService updateQuantityService;

    @Override
    @Transactional
    public void registerNewGoodsReceipt(String serviceCode, String quantityCode,
            BigDecimal quantity) {
        UUID newGoodsReceiptTrnId = UUID.randomUUID();
        GoodsReceiptTrnDto receiptDto = GoodsReceiptTrnDto.builder()
                .goodsReceiptTrnId(newGoodsReceiptTrnId).quantity(quantity).build();
        try {
            receiptDao.insert(receiptDto);
            updateQuantityService.increaseQuantity(serviceCode, quantityCode, receiptDto);
        } catch (RuntimeException e) {
            throw new TransactionalRuntimeException(e);
        }
    }
}
