package org.example.i2iacademyletcrypto1.users.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private String email;
    private String username;
    private BigDecimal cashBalance;
}
