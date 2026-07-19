package org.example.i2iacademyletcrypto1.auth.abstract_class;

import jakarta.validation.Valid;
import org.example.i2iacademyletcrypto1.auth.dto.CreateAccountDto;
import org.example.i2iacademyletcrypto1.auth.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AbstractAuthController {
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto);
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto);
}
