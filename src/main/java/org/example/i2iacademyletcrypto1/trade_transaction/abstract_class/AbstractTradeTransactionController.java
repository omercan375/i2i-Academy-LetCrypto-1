package org.example.i2iacademyletcrypto1.trade_transaction.abstract_class;

import jakarta.validation.Valid;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AbstractTradeTransactionController {
    public ResponseEntity<?> trade(@RequestHeader("Authorization") String token, @Valid @RequestBody TradeDto tradeDto);
    public ResponseEntity<?> history(@RequestHeader("Authorization") String token);
}
