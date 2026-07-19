package org.example.i2iacademyletcrypto1.ai_query_logs;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiContextDto;
import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiQueryRequestDto;
import org.example.i2iacademyletcrypto1.ai_query_logs.dto.AiQueryResponseDto;
import org.example.i2iacademyletcrypto1.ai_query_logs.prompt.AiPromptBuilder;
import org.example.i2iacademyletcrypto1.assets.AssetsRepo;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.redis.PriceCacheService;
import org.example.i2iacademyletcrypto1.trade_transaction.TradeTransactionService;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.user_assets.UserAssetsService;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.wallets.WalletsService;
import org.example.i2iacademyletcrypto1.wallets.WalletsTable;
import org.example.i2iacademyletcrypto1.zcommon.exception.AiServiceException;
import org.example.i2iacademyletcrypto1.zcommon.exception.SaveException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiQueryService {

    private static final int RECENT_TRANSACTION_LIMIT = 10;

    private final AiQueryLogsRepo aiQueryLogsRepo;
    private final LlmClient llmClient;
    private final AiPromptBuilder aiPromptBuilder;
    private final WalletsService walletsService;
    private final UserAssetsService userAssetsService;
    private final TradeTransactionService tradeTransactionService;
    private final PriceCacheService priceCacheService;
    private final AssetsRepo assetsRepo;

    public AiQueryResponseDto query(UsersTable user, AiQueryRequestDto request) {
        AiContextDto context = collectContext(user);

        String prompt = aiPromptBuilder.build(
                request.getQuery(),
                context
        );

        String aiResponse;

        try {
            aiResponse = llmClient.generateResponse(prompt);
        } catch (AiServiceException exception) {
            /*detayli hata mesajini koru (rate limit, gecersiz anahtar vb.)*/
            throw exception;
        } catch (Exception exception) {
            throw new AiServiceException(
                    "AI service is currently unavailable"
            );
        }

        AiQueryLogsTable savedLog = saveLog(
                user,
                request.getQuery(),
                aiResponse
        );

        return AiQueryResponseDto.builder()
                .queryId(savedLog.getId())
                .response(savedLog.getResponse())
                .createdAt(savedLog.getCreatedAt())
                .build();
    }

    private AiContextDto collectContext(UsersTable user) {
        WalletsTable wallet =
                walletsService.findByUserId(user.getId());

        List<PortfolioDto> portfolio =
                userAssetsService.portfolio(user);

        List<TradeHistoryDto> recentTransactions =
                tradeTransactionService.getRecentTrades(
                        user,
                        RECENT_TRANSACTION_LIMIT
                );

        Map<String, BigDecimal> currentPrices =
                collectCurrentPrices();

        return AiContextDto.builder()
                .cashBalance(wallet.getCashBalance())
                .portfolio(portfolio)
                .recentTransactions(recentTransactions)
                .currentPrices(currentPrices)
                .build();
    }

    private Map<String, BigDecimal> collectCurrentPrices() {
        List<AssetsTable> assets =
                assetsRepo.findAll();

        Map<String, BigDecimal> prices =
                new LinkedHashMap<>();

        for (AssetsTable asset : assets) {
            BigDecimal price =
                    priceCacheService.getPrice(asset.getSymbol());

            if (price != null) {
                prices.put(asset.getSymbol(), price);
            }
        }

        return prices;
    }

    @Transactional
    protected AiQueryLogsTable saveLog(
            UsersTable user,
            String query,
            String response
    ) {
        AiQueryLogsTable log = AiQueryLogsTable.builder()
                .user(user)
                .query(query)
                .response(response)
                .build();

        try {
            return aiQueryLogsRepo.save(log);
        } catch (Exception exception) {
            throw new SaveException(
                    "AI query log could not be saved"
            );
        }
    }
}