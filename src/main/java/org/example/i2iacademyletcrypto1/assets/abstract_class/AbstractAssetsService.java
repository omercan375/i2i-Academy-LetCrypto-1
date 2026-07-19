package org.example.i2iacademyletcrypto1.assets.abstract_class;


import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.assets.dto.AssetDto;

import java.util.List;

public interface AbstractAssetsService {
    public AssetsTable findAsset(String symbol);
    public void refreshAllPrices();
    public List<AssetDto> showAll();
}
