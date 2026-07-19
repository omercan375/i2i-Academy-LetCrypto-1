package org.example.i2iacademyletcrypto1.price_history.abstract_class;


import jakarta.validation.constraints.NotNull;
import org.example.i2iacademyletcrypto1.price_history.dto.PriceHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface AbstractPriceHistoryController {
    public ResponseEntity<List<PriceHistoryDto>> getPriceHistory(@RequestParam @NotNull UUID assetId);
}
