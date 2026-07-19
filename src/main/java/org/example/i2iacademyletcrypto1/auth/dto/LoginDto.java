package org.example.i2iacademyletcrypto1.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @Email(message = "email is required")
    @NotBlank(message = "email cant be blank")
    @Size(min = 5, max =255,message = "Email must be between 5 and 255 characters")
    private String email;
    @Size(min = 3, max =255,message = "Password must be at least 3 characters")
    @NotBlank(message = "password is required")
    private String password;

}

