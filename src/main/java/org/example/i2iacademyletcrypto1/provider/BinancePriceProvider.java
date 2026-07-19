package org.example.i2iacademyletcrypto1.provider;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BinancePriceProvider implements PriceProvider {

    private final RestClient restClient;

    @Override
    public BigDecimal getPrice(String symbol) {

        BinancePriceResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v3/ticker/price")
                        .queryParam("symbol", symbol + "USDT")
                        .build())
                .retrieve()
                .body(BinancePriceResponse.class);

        if (response == null || response.price() == null) {
            throw new ResourceNotFoundException("Price not found");
        }

        return response.price();
    }
}
