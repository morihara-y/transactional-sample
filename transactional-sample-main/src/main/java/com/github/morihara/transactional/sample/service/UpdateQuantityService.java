package com.github.morihara.transactional.sample.service;

import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.dto.QuantityTrnDto;

public interface UpdateQuantityService {
    QuantityTrnDto increaseQuantity(String serviceCode, String quantityCode, GoodsReceiptTrnDto receiptDto);

    QuantityTrnDto decreaseQuantity(String serviceCode, String quantityCode, GoodsIssueTrnDto issueDto);
}
