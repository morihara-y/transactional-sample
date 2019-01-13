package com.github.morihara.transactional.sample.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
public class QuantityTrnDto {
    private String quantityCode;
    private BigDecimal quantity;
}
