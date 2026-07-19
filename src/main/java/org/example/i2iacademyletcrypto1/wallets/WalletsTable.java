package org.example.i2iacademyletcrypto1.wallets;

import jakarta.persistence.*;

import lombok.*;
import org.example.i2iacademyletcrypto1.users.UsersTable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersTable user;
    @Column(name = "cash_balance")
    private BigDecimal cashBalance;
    @Version
    private Integer version;
}
