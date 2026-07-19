package org.example.i2iacademyletcrypto1.assets.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDto {
    private UUID assetId;
    private String assetName;
    private String assetSymbol;
    private BigDecimal assetPrice;
    private BigDecimal percentChange;
}
