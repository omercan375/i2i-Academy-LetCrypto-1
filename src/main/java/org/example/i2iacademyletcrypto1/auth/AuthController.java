package org.example.i2iacademyletcrypto1.auth;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.auth.abstract_class.AbstractAuthController;
import org.example.i2iacademyletcrypto1.auth.dto.CreateAccountDto;
import org.example.i2iacademyletcrypto1.auth.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AbstractAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(description = "user login")
    @Override
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/create-account")
    @Operation(description = "user create account")
    @Override
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto) {
        authService.createAccount(createAccountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
