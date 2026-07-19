package org.example.i2iacademyletcrypto1.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserNameDto {
    @NotBlank(message = "username required")
    @Size(min = 3,max = 100)
    private String oldUsername;
    @NotBlank(message = "username required")
    @Size(min = 3,max = 100)
    private String newUsername;
    @NotBlank
    @Size(min = 3,max = 255)
    private String password;
}
