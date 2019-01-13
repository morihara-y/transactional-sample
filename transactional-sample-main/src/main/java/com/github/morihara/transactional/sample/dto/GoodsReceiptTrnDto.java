package com.github.morihara.transactional.sample.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoodsReceiptTrnDto {
    private UUID goodsReceiptTrnId;
    private BigDecimal quantity;
}
