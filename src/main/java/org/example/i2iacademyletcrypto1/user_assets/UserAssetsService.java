package org.example.i2iacademyletcrypto1.user_assets;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.redis.PriceCacheService;
import org.example.i2iacademyletcrypto1.user_assets.abstract_class.AbstractUserAssetsService;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.wallets.WalletsService;
import org.example.i2iacademyletcrypto1.wallets.WalletsTable;
import org.example.i2iacademyletcrypto1.zcommon.exception.InsufficientBalanceException;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.example.i2iacademyletcrypto1.zcommon.exception.SaveException;
import org.example.i2iacademyletcrypto1.zcommon.exception.UpdateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAssetsService implements AbstractUserAssetsService {

    private final UserAssetsRepo userAssetsRepo;
    private final WalletsService walletsService;
    private final PriceCacheService priceCacheService;


    @Transactional(readOnly = true)
    @Override
    public List<PortfolioDto> portfolio(UsersTable user) {
        List<PortfolioDto> portfolio = new ArrayList<>();
        List<UserAssetsTable> userAssets = userAssetsRepo.findByUserId(user.getId());

        /*get user wallet infos*/
        WalletsTable findCashBalance = walletsService.findByUserId(user.getId());
        for (UserAssetsTable asset : userAssets) {

            /*get asset price from redis*/
            BigDecimal assetPrice = priceCacheService.getPrice(asset.getAsset().getSymbol());
            if (assetPrice == null) {
                throw new ResourceNotFoundException("Asset not found");
            }
            /*calculate total price of asset*/
            BigDecimal totalWorth =assetPrice.multiply(asset.getQuantity());

            PortfolioDto portfolioDto = PortfolioDto.builder()
                    .assetSymbol(asset.getAsset().getSymbol())
                    .cashBalance(findCashBalance.getCashBalance())
                    .totalWorth(totalWorth)
                    .assetQuantity(asset.getQuantity())
                    .build();
            portfolio.add(portfolioDto);
        }
        /*yeni kullanicida hic asset yoksa da cash balance donmeli*/
        if (portfolio.isEmpty()) {
            portfolio.add(PortfolioDto.builder()
                    .cashBalance(findCashBalance.getCashBalance())
                    .assetQuantity(BigDecimal.ZERO)
                    .totalWorth(BigDecimal.ZERO)
                    .build());
        }
        return portfolio;

    }
    @Transactional
    @Override
    public void buyAsset(UsersTable user, AssetsTable asset,BigDecimal quantity) {
        /*USER DA BU HİSSE DAHA ÖNCE VARMI YOKMU KONTROL*/
        UserAssetsTable findUserAsset = userAssetsRepo.findUserAssets(user.getId(), asset.getId());
        if (findUserAsset != null) {
            /*new quantity*/
            BigDecimal newQuantity = findUserAsset.getQuantity().add(quantity);
            int updateQuantity = userAssetsRepo.updateQuantity(newQuantity, findUserAsset.getId(), findUserAsset.getVersion());
            if (updateQuantity == 0) {
                throw new UpdateException("CANT UPDATE ASSET");
            }
        } else {
            UserAssetsTable newUserAsset = UserAssetsTable.builder()
                    .user(user)
                    .asset(asset)
                    .quantity(quantity)
                    .build();
            try {
                userAssetsRepo.save(newUserAsset);
            } catch (Exception e) {
                throw new SaveException("CANT SAVE ASSET");
            }
        }
    }
    @Transactional
    @Override
    public void sellAsset(UsersTable user, AssetsTable asset,BigDecimal quantity){
        UserAssetsTable findUserAsset = userAssetsRepo.findUserAssets(user.getId(), asset.getId());
        if (findUserAsset == null){
            throw new ResourceNotFoundException("ASSET NOT FOUND");
        }

        if(findUserAsset.getQuantity().compareTo(quantity) < 0){
            throw new InsufficientBalanceException("INSUFFICIENT BALANCE");
        }
        BigDecimal newQuantity = findUserAsset.getQuantity().subtract(quantity);
        int updateQuantity = userAssetsRepo.updateQuantity(newQuantity,findUserAsset.getId(),findUserAsset.getVersion());
        if (updateQuantity == 0){
            throw new UpdateException("CANT UPDATE ASSET");
        }
    }

    @Transactional
    @Override
    public void sellAllAsset(UsersTable user,AssetsTable asset,BigDecimal quantity){
        UserAssetsTable findUserAsset = userAssetsRepo.findUserAssets(user.getId(), asset.getId());
        if (findUserAsset == null){
            throw new ResourceNotFoundException("ASSET NOT FOUND");
        }
        if(findUserAsset.getQuantity().compareTo(quantity) == 0){
            userAssetsRepo.delete(findUserAsset);

        }else {
            throw new InsufficientBalanceException("INSUFFICIENT BALANCE");
        }
    }

}
