package com.github.morihara.transactional.sample.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuantityTrnDto {
    private String quantityCode;
    private BigDecimal quantity;
}
