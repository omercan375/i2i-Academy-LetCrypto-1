package org.example.i2iacademyletcrypto1.assets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.i2iacademyletcrypto1.assets.abstract_class.AbstractAssetsService;
import org.example.i2iacademyletcrypto1.assets.dto.AssetDto;
import org.example.i2iacademyletcrypto1.price_history.PriceHistoryRepo;
import org.example.i2iacademyletcrypto1.price_history.PriceHistoryService;
import org.example.i2iacademyletcrypto1.price_history.PriceHistoryTable;
import org.example.i2iacademyletcrypto1.provider.PriceProvider;
import org.example.i2iacademyletcrypto1.redis.PriceCacheService;
import org.example.i2iacademyletcrypto1.user_assets.UserAssetsService;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetsService implements AbstractAssetsService {
    private final AssetsRepo assetsRepo;
    private final PriceProvider priceProvider;
    private final PriceHistoryService priceHistoryService;
    private final PriceCacheService priceCacheService;
    private final PriceHistoryRepo priceHistoryRepo;
    private final UserAssetsService userAssetsService;

    @Override
    public AssetsTable findAsset(String symbol) {
        AssetsTable findAsset = assetsRepo.findBySymbol(symbol);
        if (findAsset == null) {
            throw new ResourceNotFoundException("Asset not found");
        }
        return findAsset;
    }


    @Override
    public void refreshAllPrices() {
        Iterable<AssetsTable> assets = assetsRepo.findAll();

        for (AssetsTable asset : assets) {

            try {
                BigDecimal freshPrice = priceProvider.getPrice(asset.getSymbol());

                priceHistoryService.refreshPrice(asset, freshPrice);   // price history'ye kayıt
                priceCacheService.savePrice(asset.getSymbol(), freshPrice);   // Redis'e yaz
            } catch (Exception e) {
                log.warn("Price update failed for {}: {}", asset.getSymbol(), e.getMessage());
            }
        }
    }
    @Transactional(readOnly = true)
    @Override
    public List<AssetDto> showAll() {
        List<AssetDto> allAssets = new ArrayList<>();
        List<AssetsTable> findAll = assetsRepo.findAll();
        BigDecimal percentChange;
        for (AssetsTable asset : findAll) {
            BigDecimal freshPrice = priceCacheService.getPrice(asset.getSymbol());
            // find percent change
            PriceHistoryTable findOne = priceHistoryRepo.findOneByAssetId(asset.getId());
            if (findOne == null) {
                percentChange = BigDecimal.ZERO;
            }else{
                percentChange = findOne.getChangePercent();
            }
            AssetDto assetDto = AssetDto.builder()
                    .assetId(asset.getId())
                    .assetName(asset.getName())
                    .assetSymbol(asset.getSymbol())
                    .assetPrice(freshPrice)
                    .percentChange(percentChange)
                    .build();
            allAssets.add(assetDto);
        }
        return allAssets;
    }

}
