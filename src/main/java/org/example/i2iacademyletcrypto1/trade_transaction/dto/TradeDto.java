package org.example.i2iacademyletcrypto1.trade_transaction.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.i2iacademyletcrypto1.trade_transaction.TradeType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeDto {
    @NotBlank(message = "Asset symbol is required")
    private String assetSymbol;
    @NotNull(message = "Trade type is required")
    private TradeType tradeType;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal cryptoAmount;


}

