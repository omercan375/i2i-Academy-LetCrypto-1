package org.example.i2iacademyletcrypto1.price_history;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.price_history.abstract_class.AbstractPriceHistoryController;
import org.example.i2iacademyletcrypto1.price_history.dto.PriceHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
public class PriceHistoryController implements AbstractPriceHistoryController {

    private final PriceHistoryService priceHistoryService;



    @GetMapping("/history")
    @Operation(description = "price history")
    @Override
    public ResponseEntity<List<PriceHistoryDto>> getPriceHistory(@RequestParam @NotNull UUID assetId){
       List<PriceHistoryDto> history = priceHistoryService.getPriceHistory(assetId);
       return ResponseEntity.ok(history);

    }
}
