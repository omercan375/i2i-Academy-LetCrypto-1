package org.example.i2iacademyletcrypto1.trade_transaction.abstract_class;

import org.example.i2iacademyletcrypto1.trade_transaction.TradeTransactionTable;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeDto;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.users.UsersTable;

import java.util.List;

public interface AbstractTradeTransactionService {
    public void trade(UsersTable user, TradeDto tradeDto);
    public List<TradeHistoryDto> tradeHistory(UsersTable user);
    public List<TradeHistoryDto> getRecentTrades(UsersTable user, int limit);


}
