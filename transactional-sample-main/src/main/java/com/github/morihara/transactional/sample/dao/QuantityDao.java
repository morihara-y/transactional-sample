package com.github.morihara.transactional.sample.dao;

import java.util.Optional;
import com.github.morihara.transactional.sample.dto.QuantityTrnDto;

public interface QuantityDao {
    void insert(QuantityTrnDto dto);

    void update(QuantityTrnDto dto);

    void remove(String quantityCode);

    Optional<QuantityTrnDto> getQuantity(String quantityCode);
}
