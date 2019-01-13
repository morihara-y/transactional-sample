package com.github.morihara.transactional.sample.dao;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsReceiptDaoImpl implements GoodsReceiptDao {
    private final JdbcTemplate jdbc;

    private static final RowMapper<GoodsReceiptTrnDto> ROW_MAPPER = (rs, i) -> {
        UUID trnId = UUID.fromString(rs.getString("goods_receipt_trn_id"));
        return GoodsReceiptTrnDto.builder().goodsReceiptTrnId(trnId)
                .quantity(rs.getBigDecimal("quantity")).build();
    };

    @Override
    public void insert(GoodsReceiptTrnDto dto) {
        jdbc.update("insert into goods_receipt_trn (goods_receipt_trn_id, quantity) values (?, ?)",
                dto.getGoodsReceiptTrnId().toString(), dto.getQuantity());
    }

    @Override
    public List<GoodsReceiptTrnDto> getAll() {
        String sql = "select * from goods_receipt_trn";
        return jdbc.query(sql, ROW_MAPPER);
    }
}
