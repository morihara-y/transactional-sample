package com.github.morihara.transactional.sample.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsIssueDaoImpl implements GoodsIssueDao {
    private final JdbcTemplate jdbc;

    private static final RowMapper<GoodsIssueTrnDto> ROW_MAPPER = (rs, i) -> {
        UUID trnId = UUID.fromString(rs.getString("goods_issue_trn_id"));
        return GoodsIssueTrnDto.builder().goodsIssueTrnId(trnId)
                .quantity(rs.getBigDecimal("quantity")).build();
    };

    @Override
    public void insert(GoodsIssueTrnDto dto) {
        jdbc.update("insert into goods_issue_trn (goods_issue_trn_id, quantity) values (?, ?)",
                dto.getGoodsIssueTrnId().toString(), dto.getQuantity());
    }

    @Override
    public List<GoodsIssueTrnDto> getAll() {
        String sql = "select * from goods_issue_trn";
        return jdbc.query(sql, ROW_MAPPER);
    }
}
