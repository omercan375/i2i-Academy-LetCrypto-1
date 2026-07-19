package org.example.i2iacademyletcrypto1.price_history;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.price_history.abstract_class.AbstractPriceHistoryService;
import org.example.i2iacademyletcrypto1.price_history.dto.PriceHistoryDto;
import org.example.i2iacademyletcrypto1.redis.PriceCacheService;
import org.example.i2iacademyletcrypto1.zcommon.exception.SaveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PriceHistoryService implements AbstractPriceHistoryService {

    private final PriceCacheService priceCacheService;
    private final PriceHistoryRepo priceHistoryRepo;


    @Transactional
    @Override
    public void refreshPrice(AssetsTable asset, BigDecimal newPrice) {
        BigDecimal lastPrice;
        BigDecimal changePercent;
        /*redis den en son ki fiyatı 'changePercent' hazırlamak için çek*/
        lastPrice = priceCacheService.getPrice(asset.getSymbol());
        if (lastPrice == null || lastPrice.compareTo(BigDecimal.ZERO) == 0) {
            changePercent = BigDecimal.ZERO;
        } else {
            changePercent = ((newPrice.subtract(lastPrice).divide(lastPrice, 10, RoundingMode.HALF_DOWN)).multiply(BigDecimal.valueOf(100)));
        }
        /*YENİ FİYATI HİSTROYE KAYIT ET*/
        PriceHistoryTable newPriceHistory = PriceHistoryTable.builder()
                .asset(asset)
                .price(newPrice)
                .changePercent(changePercent)
                .build();
        try {
            priceHistoryRepo.save(newPriceHistory);
        } catch (Exception e) {
            throw new SaveException("PRİCE HİSTORY CANT SAVED PLEASE TRY AGAIN");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<PriceHistoryDto> getPriceHistory(UUID assetId) {
        List<PriceHistoryDto> priceHistory =  new ArrayList<>();
        List<PriceHistoryTable> priceHistoryTables = priceHistoryRepo.findAllByAssetId(assetId);
        for (PriceHistoryTable priceHistoryTable : priceHistoryTables) {
            String percent = "%" + priceHistoryTable.getChangePercent().toPlainString();
            PriceHistoryDto priceHistoryDto = PriceHistoryDto.builder()
                    .price(priceHistoryTable.getPrice())
                    .recordedAt(priceHistoryTable.getRecordedAt())
                    .changePercent(percent)
                    .build();
            priceHistory.add(priceHistoryDto);
        }
        return priceHistory;
    }

}
