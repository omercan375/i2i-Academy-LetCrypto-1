package org.example.i2iacademyletcrypto1.price_history.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceHistoryDto {
    private BigDecimal price;
    private String changePercent;
    private LocalDateTime recordedAt;
}
