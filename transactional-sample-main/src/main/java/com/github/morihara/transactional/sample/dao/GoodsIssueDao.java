package com.github.morihara.transactional.sample.dao;

import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;

public interface GoodsIssueDao {
    void insert(GoodsIssueTrnDto dto);
}
