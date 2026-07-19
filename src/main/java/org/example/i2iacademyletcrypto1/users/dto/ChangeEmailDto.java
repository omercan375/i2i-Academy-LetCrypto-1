package org.example.i2iacademyletcrypto1.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeEmailDto {
    @Email(message = "email required")
    @NotBlank(message = "email cant be blank")
    @Size(min = 3,max = 255)
    private String oldEmail;
    @Email(message = "email required")
    @NotBlank(message = "email cant be blank")
    @Size(min = 3,max = 255)
    private String newEmail;
    @NotBlank(message = "password cant be blank")
    @Size(min = 3,max = 255)
    private String password;




}
