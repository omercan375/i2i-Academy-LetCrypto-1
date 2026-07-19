package org.example.i2iacademyletcrypto1.user_assets;

import jakarta.persistence.*;
import lombok.*;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_assets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAssetsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersTable user;
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetsTable asset;
    @Column(name = "quantity")
    private BigDecimal quantity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Version
    private Integer version;
}
