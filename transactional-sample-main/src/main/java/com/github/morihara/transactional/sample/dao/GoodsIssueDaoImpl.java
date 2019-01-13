package com.github.morihara.transactional.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsIssueDaoImpl implements GoodsIssueDao {
    private final JdbcTemplate jdbc;

    @Override
    public void insert(GoodsIssueTrnDto dto) {
        jdbc.update("insert into goods_issue_trn (goods_issue_trn_id, quantity) values (?, ?)",
                dto.getGoodsIssueTrnId().toString(), dto.getQuantity());
    }
}
