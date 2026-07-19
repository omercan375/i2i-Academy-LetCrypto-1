package org.example.i2iacademyletcrypto1.assets;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "name")
    private String name;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
