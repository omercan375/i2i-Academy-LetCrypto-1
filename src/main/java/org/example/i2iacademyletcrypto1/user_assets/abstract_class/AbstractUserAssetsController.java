package org.example.i2iacademyletcrypto1.user_assets.abstract_class;

import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface AbstractUserAssetsController {
    public ResponseEntity<List<PortfolioDto>> getPortfolio(@RequestHeader("Authorization") String token);
}
