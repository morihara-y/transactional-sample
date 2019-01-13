package com.github.morihara.transactional.sample.service;

import java.math.BigDecimal;

public interface GoodsReceiptService {
    void registerNewGoodsReceipt(String serviceCode, String quantityCode, BigDecimal quantity);
}
