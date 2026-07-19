package org.example.i2iacademyletcrypto1.assets;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.assets.abstract_class.AbstractAssetsController;
import org.example.i2iacademyletcrypto1.assets.dto.AssetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/asset-service")
@RequiredArgsConstructor
public class AssetsController implements AbstractAssetsController {
    private final AssetsService assetsService;

    @GetMapping("/findAll")
    @Operation(summary = "find all assets")
    @Override
    public ResponseEntity<List<AssetDto>>  findAllAssets() {
        List<AssetDto> assetDto = assetsService.showAll();
        return ResponseEntity.ok(assetDto);
    }
}
