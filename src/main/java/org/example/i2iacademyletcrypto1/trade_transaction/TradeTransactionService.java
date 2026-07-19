package org.example.i2iacademyletcrypto1.trade_transaction;


import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.assets.AssetsService;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.redis.PriceCacheService;
import org.example.i2iacademyletcrypto1.trade_transaction.abstract_class.AbstractTradeTransactionService;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeDto;
import org.example.i2iacademyletcrypto1.trade_transaction.dto.TradeHistoryDto;
import org.example.i2iacademyletcrypto1.user_assets.UserAssetsService;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.wallets.WalletsService;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.example.i2iacademyletcrypto1.zcommon.exception.SaveException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeTransactionService implements AbstractTradeTransactionService {

    private final PriceCacheService priceCacheService;
    private final WalletsService walletsService;
    private final AssetsService assetsService;
    private final TradeTransactionRepo tradeTransactionRepo;
    private final UserAssetsService userAssetsService;

    @Transactional
    @Override
    public void trade(UsersTable user, TradeDto tradeDto) {

        String normalizedSymbol =
                tradeDto.getAssetSymbol().trim().toUpperCase();

        AssetsTable asset =
                assetsService.findAsset(normalizedSymbol);

        BigDecimal assetPrice =
                priceCacheService.getPrice(normalizedSymbol);

        if (assetPrice == null) {
            throw new ResourceNotFoundException(
                    "Asset price not found in Redis"
            );
        }

        BigDecimal totalAmount =
                assetPrice.multiply(tradeDto.getCryptoAmount());

        switch (tradeDto.getTradeType()) {
            case BUY :
                walletsService.buyAsset(user.getId(), totalAmount);
                userAssetsService.buyAsset(user,asset, tradeDto.getCryptoAmount());
                break;


            case SELL :
                userAssetsService.sellAsset(user,asset,tradeDto.getCryptoAmount());
                walletsService.sellAsset(user.getId(), totalAmount);
                break;



        }

        TradeTransactionTable transaction =
                TradeTransactionTable.builder()
                        .user(user)
                        .asset(asset)
                        .tradeType(tradeDto.getTradeType())
                        .cryptoAmount(tradeDto.getCryptoAmount())
                        .executionPrice(assetPrice)
                        .totalAmount(totalAmount)
                        .build();

        try {
            tradeTransactionRepo.save(transaction);
        } catch (Exception exception) {
            throw new SaveException(
                    "Trade transaction could not be saved"
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TradeHistoryDto> tradeHistory(UsersTable user) {

        return tradeTransactionRepo
                .findByUserId(user.getId())
                .stream()
                .map(this::toHistoryDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TradeHistoryDto> getRecentTrades(
            UsersTable user,
            int limit
    ) {
        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Trade history limit must be greater than zero"
            );
        }

        return tradeTransactionRepo
                .findRecentByUserId(
                        user.getId(),
                        PageRequest.of(0, limit)
                )
                .stream()
                .map(this::toHistoryDto)
                .toList();
    }

    private TradeHistoryDto toHistoryDto(
            TradeTransactionTable transaction
    ) {
        return TradeHistoryDto.builder()
                .assetSymbol(transaction.getAsset().getSymbol())
                .assetName(transaction.getAsset().getName())
                .tradeType(transaction.getTradeType().name())
                .cryptoAmount(transaction.getCryptoAmount())
                .executionPrice(transaction.getExecutionPrice())
                .totalAmount(transaction.getTotalAmount())
                .dateTime(transaction.getExecutedAt())
                .build();
    }
}