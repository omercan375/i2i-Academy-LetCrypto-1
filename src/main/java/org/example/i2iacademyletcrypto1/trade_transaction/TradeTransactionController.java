package org.example.i2iacademyletcrypto1.trade_transaction;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.i2iacademyletcrypto1.trade_transaction.abstract_class.AbstractTradeTransactionController;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeDto;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.users.UsersService;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TradeTransactionController implements AbstractTradeTransactionController {

    private final TradeTransactionService tradeTransactionService;
    private final UsersService usersService;

    @PostMapping("/trade")
    @Operation(description = "buy or sell asset")
    @Override
    public ResponseEntity<?> trade(@RequestHeader("Authorization") String token, @Valid @RequestBody TradeDto tradeDto) {
        UsersTable findUser = usersService.findUserById(token);
        tradeTransactionService.trade(findUser, tradeDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    @Operation(description = "transaction history (buy or sell)")
    @Override
    public ResponseEntity<?> history(@RequestHeader("Authorization") String token) {
        UsersTable findUser = usersService.findUserById(token);
        List<TradeHistoryDto> history = tradeTransactionService.tradeHistory(findUser);
        return ResponseEntity.ok().body(history);
    }
}
