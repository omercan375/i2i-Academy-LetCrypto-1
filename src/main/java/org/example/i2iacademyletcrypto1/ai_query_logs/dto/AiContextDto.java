package org.example.i2iacademyletcrypto1.ai_query_logs.dto;

import lombok.*;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiContextDto {

    private BigDecimal cashBalance;

    private List<PortfolioDto> portfolio;

    private List<TradeHistoryDto> recentTransactions;

    private Map<String, BigDecimal> currentPrices;
}