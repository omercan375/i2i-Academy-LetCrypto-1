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
public class CreateAccountDto {
    @Email(message = "email is required")
    @NotBlank(message = "email cant be blank")
    @Size(min = 5 , max = 255,message = "email must be more than 4 words")
    private String email;
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 100,message = "username must be more than 2 words")
    private String username;
    @NotBlank(message = "password is required")
    @Size(min = 3, max = 255,message = "password must be more than 2 words")
    private String password;
}
