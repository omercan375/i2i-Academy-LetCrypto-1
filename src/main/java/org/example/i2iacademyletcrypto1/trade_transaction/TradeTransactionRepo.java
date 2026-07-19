package org.example.i2iacademyletcrypto1.trade_transaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TradeTransactionRepo
        extends CrudRepository<TradeTransactionTable, UUID> {

    @Query("""
            SELECT t
            FROM TradeTransactionTable t
            JOIN FETCH t.asset
            WHERE t.user.id = :userId
            ORDER BY t.executedAt DESC
            """)
    List<TradeTransactionTable> findByUserId(
            @Param("userId") UUID userId
    );

    @Query("""
            SELECT t
            FROM TradeTransactionTable t
            JOIN FETCH t.asset
            WHERE t.user.id = :userId
            ORDER BY t.executedAt DESC
            """)
    List<TradeTransactionTable> findRecentByUserId(
            @Param("userId") UUID userId,
            Pageable pageable
    );
}