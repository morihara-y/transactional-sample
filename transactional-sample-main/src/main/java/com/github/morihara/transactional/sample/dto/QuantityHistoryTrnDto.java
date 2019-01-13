package com.github.morihara.transactional.sample.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuantityHistoryTrnDto {
    private UUID quantityHistoryTrnId;
    private String quantityCode;
    private String serviceCode;
    private UUID receiptIssueTrnId;
    private BigDecimal changeQuantity;
}
