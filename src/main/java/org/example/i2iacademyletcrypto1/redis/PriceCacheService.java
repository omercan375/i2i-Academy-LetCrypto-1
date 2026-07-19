package org.example.i2iacademyletcrypto1.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceCacheService {

    private static final String PRICE_KEY_PREFIX = "price:";

    private final RedisTemplate<String, BigDecimal> assetPrice;

    public PriceCacheService(@Qualifier("assetPrice") RedisTemplate<String, BigDecimal> assetPrice) {
        this.assetPrice = assetPrice;
    }

    // KAYIT
    public void savePrice(String symbol, BigDecimal price) {
        assetPrice.opsForValue().set(PRICE_KEY_PREFIX + symbol, price);
    }

    // ÇEKME
    public BigDecimal getPrice(String symbol) {
        return assetPrice.opsForValue().get(PRICE_KEY_PREFIX + symbol);
    }
}
