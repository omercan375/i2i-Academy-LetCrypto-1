package org.example.i2iacademyletcrypto1.wallets;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.zcommon.exception.AlreadyExistException;
import org.example.i2iacademyletcrypto1.zcommon.exception.InsufficientBalanceException;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.example.i2iacademyletcrypto1.zcommon.exception.UpdateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class WalletsService {

    private final WalletsRepo walletsRepo;


    /*create wallet for new customer*/
    public void createWallet(UsersTable user) {
        WalletsTable wallet = WalletsTable.builder()
                .user(user)
                .cashBalance(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10_000, 100_000)).setScale(2, RoundingMode.HALF_UP))
                .build();
        try {
            walletsRepo.save(wallet);
        } catch (Exception e) {
            throw new AlreadyExistException("WALLET ALREADY EXISTS");
        }
    }

    public WalletsTable findByUserId(UUID id) {
        WalletsTable findWallet = walletsRepo.findByUserId(id);
        if (findWallet == null) {
            throw new ResourceNotFoundException("WALLET NOT FOUND");
        }
        return findWallet;
    }


    public void buyAsset(UUID userId, BigDecimal totalAmount) {
        WalletsTable findWallet = findByUserId(userId);

        if (findWallet.getCashBalance().compareTo(totalAmount) >= 0) {
            /*yeni cashbalance ı hesapla*/
            BigDecimal newCashBalance = findWallet.getCashBalance().subtract(totalAmount);
            int updateBalance = walletsRepo.updateBalance(newCashBalance,findWallet.getId(),findWallet.getVersion());
            if (updateBalance == 0 ) {
                throw new UpdateException("CANT UPDATE BALANCE");
            }
        } else {
            throw new InsufficientBalanceException("INSUFFICENT BALANCE");
        }
    }

    public void sellAsset(UUID userId, BigDecimal totalAmount) {
        WalletsTable findWallet = findByUserId(userId);

        BigDecimal newCashBalance = findWallet.getCashBalance().add(totalAmount);
        int updateBalance =  walletsRepo.updateBalance(newCashBalance,findWallet.getId(),findWallet.getVersion());
        if (updateBalance == 0 ) {
            throw new UpdateException("CANT UPDATE BALANCE");
        }

    }

}
