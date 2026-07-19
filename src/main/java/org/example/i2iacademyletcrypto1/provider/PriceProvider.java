package org.example.i2iacademyletcrypto1.provider;

import java.math.BigDecimal;

public interface PriceProvider {
    BigDecimal getPrice(String symbol);

}

