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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletsService {

    private final WalletsRepo walletsRepo;


    /*create wallet for new customer*/
    @Transactional
    public void createWallet(UsersTable user) {
        WalletsTable wallet = WalletsTable.builder()
                .user(user)
                .cashBalance(BigDecimal.valueOf(1000000000))
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

    @Transactional
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
    @Transactional
    public void sellAsset(UUID userId, BigDecimal totalAmount) {
        WalletsTable findWallet = findByUserId(userId);

        BigDecimal newCashBalance = findWallet.getCashBalance().add(totalAmount);
        int updateBalance =  walletsRepo.updateBalance(newCashBalance,findWallet.getId(),findWallet.getVersion());
        if (updateBalance == 0 ) {
            throw new UpdateException("CANT UPDATE BALANCE");
        }

    }

}
