package com.github.morihara.transactional.sample.dao;

import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;

public interface GoodsReceiptDao {
    void insert(GoodsReceiptTrnDto dto);
}
