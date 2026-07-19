package org.example.i2iacademyletcrypto1.price_history.abstract_class;

import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.price_history.dto.PriceHistoryDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AbstractPriceHistoryService {
    public void refreshPrice(AssetsTable asset, BigDecimal newPrice);
    public List<PriceHistoryDto> getPriceHistory(UUID assetId);
}
