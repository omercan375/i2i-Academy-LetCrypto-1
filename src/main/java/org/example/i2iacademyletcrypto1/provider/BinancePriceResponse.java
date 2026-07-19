package org.example.i2iacademyletcrypto1.provider;

import java.math.BigDecimal;

public record BinancePriceResponse(
        String symbol,
        BigDecimal price
) {
}
