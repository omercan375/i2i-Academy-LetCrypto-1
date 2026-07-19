package org.example.i2iacademyletcrypto1.trade_transaction;

import jakarta.persistence.*;
import lombok.*;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trade_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeTransactionTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersTable user;
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetsTable  asset;
    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type")
    private TradeType tradeType;
    @Column(name = "crypto_amount")
    private BigDecimal cryptoAmount;
    @Column(name = "execution_price")
    private BigDecimal executionPrice;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @CreationTimestamp
    @Column(name = "executed_at")
    private LocalDateTime executedAt;




}
