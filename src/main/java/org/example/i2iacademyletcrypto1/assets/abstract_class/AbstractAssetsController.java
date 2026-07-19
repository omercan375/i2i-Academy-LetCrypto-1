package org.example.i2iacademyletcrypto1.assets.abstract_class;

import org.example.i2iacademyletcrypto1.assets.dto.AssetDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AbstractAssetsController {
    public ResponseEntity<List<AssetDto>> findAllAssets();

}
