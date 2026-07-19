package org.example.i2iacademyletcrypto1.trade_transaction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeHistoryDto {
    private String assetSymbol;
    private String assetName;
    private String tradeType;
    private BigDecimal cryptoAmount;
    private BigDecimal executionPrice;
    private BigDecimal totalAmount;
    private LocalDateTime dateTime;
}
