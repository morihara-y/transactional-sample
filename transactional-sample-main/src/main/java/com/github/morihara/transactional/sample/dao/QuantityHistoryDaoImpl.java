package com.github.morihara.transactional.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.QuantityHistoryTrnDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuantityHistoryDaoImpl implements QuantityHistoryDao {
    private final JdbcTemplate jdbc;

    @Override
    public void insert(QuantityHistoryTrnDto dto) {
        jdbc.update(
                "insert into quantity_history_trn (quantity_history_trn_id, quantity_code, service_code, receipt_issue_trn_id, change_quantity) values (?, ?, ?, ?, ?)",
                dto.getQuantityHistoryTrnId().toString(), dto.getQuantityCode(), dto.getServiceCode(),
                dto.getReceiptIssueTrnId().toString(), dto.getChangeQuantity());
    }
}
