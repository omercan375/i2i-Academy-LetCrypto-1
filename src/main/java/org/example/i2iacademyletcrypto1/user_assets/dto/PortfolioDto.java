package org.example.i2iacademyletcrypto1.user_assets.dto;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDto {
    private String assetSymbol;
    private BigDecimal cashBalance;
    private BigDecimal assetQuantity;
    private BigDecimal totalWorth;
    private BigDecimal changedWorth;

}
