package com.github.morihara.transactional.sample.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.dto.QuantityHistoryTrnDto;
import com.github.morihara.transactional.sample.dto.QuantityTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Slf4j
public class UpdateQuantityServiceImpl implements UpdateQuantityService {
    private final QuantityHistoryDao quantityHistoryDao;
    private final QuantityDao quantityDao;

    @Override
    public QuantityTrnDto increaseQuantity(String serviceCode, String quantityCode,
            GoodsReceiptTrnDto receiptDto) {
        BigDecimal changeQuantity = receiptDto.getQuantity();
        try {
            QuantityTrnDto result = upsertQuantity(quantityCode, changeQuantity); 
            registerQuantityHistory(serviceCode, quantityCode, receiptDto.getGoodsReceiptTrnId(),
                    changeQuantity);
            return result;
        } catch (RuntimeException e) {
            log.error("Error occurred in update quantity");
            throw new TransactionalRuntimeException(e);
        }
    }

    @Override
    public QuantityTrnDto decreaseQuantity(String serviceCode, String quantityCode,
            GoodsIssueTrnDto issueDto) {
        BigDecimal changeQuantity = BigDecimal.ZERO.subtract(issueDto.getQuantity());
        try {
            QuantityTrnDto result = upsertQuantity(quantityCode, changeQuantity); 
            registerQuantityHistory(serviceCode, quantityCode, issueDto.getGoodsIssueTrnId(),
                    changeQuantity);
            return result;
        } catch (RuntimeException e) {
            log.error("Error occurred in update quantity");
            throw new TransactionalRuntimeException(e);
        }
    }

    private void registerQuantityHistory(String serviceCode, String quantityCode,
            UUID receiptIssueTrnId, BigDecimal changeQuantity) {
        UUID newQuantityHistoryTrnId = UUID.randomUUID();
        QuantityHistoryTrnDto historyDto = QuantityHistoryTrnDto.builder()
                .quantityHistoryTrnId(newQuantityHistoryTrnId).quantityCode(quantityCode)
                .serviceCode(serviceCode).receiptIssueTrnId(receiptIssueTrnId)
                .changeQuantity(changeQuantity).build();
        quantityHistoryDao.insert(historyDto);
    }

    private QuantityTrnDto upsertQuantity(String quantityCode, BigDecimal changeQuantity) {
        QuantityTrnDto newQuantityTrnDto = QuantityTrnDto.builder().quantityCode(quantityCode)
                .quantity(changeQuantity).build();
        Optional<QuantityTrnDto> latestQuantityDto = quantityDao.getQuantity(quantityCode);
        if (!latestQuantityDto.isPresent()) {
            quantityDao.insert(newQuantityTrnDto);
            return newQuantityTrnDto;
        }

        BigDecimal calculatedQuantity = latestQuantityDto.get().getQuantity().add(changeQuantity);
        newQuantityTrnDto.setQuantity(calculatedQuantity);
        quantityDao.update(newQuantityTrnDto);
        return newQuantityTrnDto;
    }
}
