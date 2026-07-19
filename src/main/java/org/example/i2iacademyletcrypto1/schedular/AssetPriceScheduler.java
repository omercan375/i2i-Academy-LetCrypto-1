package org.example.i2iacademyletcrypto1.schedular;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.assets.AssetsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetPriceScheduler {

    private final AssetsService assetsService;

    @Scheduled(fixedDelay = 15000)
    public void updatePrices() {
        assetsService.refreshAllPrices();
    }

}
