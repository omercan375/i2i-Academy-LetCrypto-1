package org.example.i2iacademyletcrypto1.wallets;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletsRepo extends CrudRepository<WalletsTable,UUID> {
    @Query("SELECT w FROM WalletsTable w WHERE w.user.id=:userId")
    WalletsTable findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE WalletsTable w SET w.cashBalance=:newCashBalance,w.version=w.version + 1 WHERE w.id=:walletId and w.version=:version")
    int updateBalance(@Param("newCashBalance") BigDecimal newCashBalance, @Param("walletId") UUID walletId, @Param("version") int version);

}
