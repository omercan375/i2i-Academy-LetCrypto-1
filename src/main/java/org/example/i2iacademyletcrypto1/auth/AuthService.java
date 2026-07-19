package org.example.i2iacademyletcrypto1.auth;

import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.example.i2iacademyletcrypto1.auth.abstract_class.AbstractAuthController;
import org.example.i2iacademyletcrypto1.auth.abstract_class.AbstractAuthService;
import org.example.i2iacademyletcrypto1.auth.dto.CreateAccountDto;
import org.example.i2iacademyletcrypto1.auth.dto.LoginDto;
import org.example.i2iacademyletcrypto1.redis.SessionService;
import org.example.i2iacademyletcrypto1.users.UsersRepo;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.example.i2iacademyletcrypto1.wallets.WalletsService;
import org.example.i2iacademyletcrypto1.zcommon.exception.AlreadyExistException;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AbstractAuthService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final WalletsService walletsService;

    @Transactional(readOnly = true)
    @Override
    public String login(LoginDto loginDto) {
        /*email control*/
        UsersTable emailControl = usersRepo.findByEmail(loginDto.getEmail());
        if (emailControl == null) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        boolean passwordControl = passwordEncoder.matches(loginDto.getPassword(), emailControl.getPassword());
        if (!passwordControl) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        return sessionService.createSession(emailControl.getId());
    }

     @Transactional
     @Override
    public void createAccount(CreateAccountDto createAccountDto) {
        UsersTable user;
        /*email control*/
        UsersTable emailControl = usersRepo.findByEmail(createAccountDto.getEmail());
        if (emailControl != null) {
            throw new AlreadyExistException("Email already exists");
        }
        /*password hash*/
        String passwordHash = passwordEncoder.encode(createAccountDto.getPassword());
        UsersTable newUser = UsersTable.builder()
                .email(createAccountDto.getEmail())
                .password(passwordHash)
                .username(createAccountDto.getUsername())
                .build();

         user = usersRepo.save(newUser);


        /*create wallet*/
        walletsService.createWallet(user);
    }
}
