package com.github.morihara.transactional.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsReceiptDaoImpl implements GoodsReceiptDao {
    private final JdbcTemplate jdbc;

    @Override
    public void insert(GoodsReceiptTrnDto dto) {
        jdbc.update("insert into goods_receipt_trn (goods_receipt_trn_id, quantity) values (?, ?)",
                dto.getGoodsReceiptTrnId().toString(), dto.getQuantity());
    }
}
