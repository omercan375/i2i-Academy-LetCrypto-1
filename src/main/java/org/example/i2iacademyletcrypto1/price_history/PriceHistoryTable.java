package org.example.i2iacademyletcrypto1.price_history;

import jakarta.persistence.*;
import lombok.*;
import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceHistoryTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetsTable asset;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "change_percent")
    private BigDecimal changePercent;
    @CreationTimestamp
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}
