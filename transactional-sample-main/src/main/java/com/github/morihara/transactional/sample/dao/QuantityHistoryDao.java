package com.github.morihara.transactional.sample.dao;

import com.github.morihara.transactional.sample.dto.QuantityHistoryTrnDto;

public interface QuantityHistoryDao {
    void insert(QuantityHistoryTrnDto dto);
}
