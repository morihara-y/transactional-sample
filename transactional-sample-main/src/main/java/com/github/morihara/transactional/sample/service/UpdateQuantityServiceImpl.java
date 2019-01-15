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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class UpdateQuantityServiceImpl implements UpdateQuantityService {
    private final QuantityHistoryDao quantityHistoryDao;
    private final QuantityDao quantityDao;

    @Override
    public QuantityTrnDto increaseQuantity(String serviceCode, String quantityCode,
            GoodsReceiptTrnDto receiptDto) {
        BigDecimal changeQuantity = receiptDto.getQuantity();
        QuantityTrnDto result = upsertQuantity(quantityCode, changeQuantity);
        registerQuantityHistory(serviceCode, quantityCode, receiptDto.getGoodsReceiptTrnId(),
                changeQuantity);
        return result;
    }

    @Override
    public QuantityTrnDto decreaseQuantity(String serviceCode, String quantityCode,
            GoodsIssueTrnDto issueDto) {
        BigDecimal changeQuantity = BigDecimal.ZERO.subtract(issueDto.getQuantity());
        QuantityTrnDto result = upsertQuantity(quantityCode, changeQuantity);
        registerQuantityHistory(serviceCode, quantityCode, issueDto.getGoodsIssueTrnId(),
                changeQuantity);
        return result;
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
