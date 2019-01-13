package com.github.morihara.transactional.sample.service;

import java.math.BigDecimal;

public interface GoodsIssueService {
    void registerNewGoodsIssue(String serviceCode, String quantityCode, BigDecimal quantity);
}
