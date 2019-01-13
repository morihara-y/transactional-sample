package com.github.morihara.transactional.sample.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.github.morihara.transactional.sample.dto.QuantityTrnDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuantityDaoImpl implements QuantityDao {
    private final JdbcTemplate jdbc;

    private static final RowMapper<QuantityTrnDto> ROW_MAPPER = (rs, i) -> {
        return QuantityTrnDto.builder().quantityCode(rs.getString("quantity_code"))
                .quantity(rs.getBigDecimal("quantity")).build();
    };

    @Override
    public void insert(QuantityTrnDto dto) {
        jdbc.update("insert into quantity_trn (quantity_code, quantity) values (?, ?)",
                dto.getQuantityCode(), dto.getQuantity());
    }

    @Override
    public void update(QuantityTrnDto dto) {
        jdbc.update("update quantity_trn set quantity = ? where quantity_code = ?",
                dto.getQuantity(), dto.getQuantityCode());
    }

    @Override
    public Optional<QuantityTrnDto> getQuantity(String quantityCode) {
        String sql = "select quantity_code, quantity from quantity_trn where quantity_code = ?";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, quantityCode);
            }
        };
        List<QuantityTrnDto> quantityTrns = jdbc.query(sql, pss, ROW_MAPPER);
        if (quantityTrns.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(quantityTrns.get(0));
    }
}
