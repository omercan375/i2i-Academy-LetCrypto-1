package org.example.i2iacademyletcrypto1.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {
    @NotBlank(message = "password required")
    @Size(min = 3,max = 255)
    private String oldPassword;
    @NotBlank(message = "password required")
    @Size(min = 3,max = 255)
    private String newPassword;

}
