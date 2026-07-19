package org.example.i2iacademyletcrypto1.auth.abstract_class;

import org.example.i2iacademyletcrypto1.auth.dto.CreateAccountDto;
import org.example.i2iacademyletcrypto1.auth.dto.LoginDto;

public interface AbstractAuthService {
    public String login(LoginDto loginDto);
    public void createAccount(CreateAccountDto createAccountDto);
}
