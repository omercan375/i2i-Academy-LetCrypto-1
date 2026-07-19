package org.example.i2iacademyletcrypto1.users;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.redis.SessionService;
import org.example.i2iacademyletcrypto1.users.dto.ChangeEmailDto;
import org.example.i2iacademyletcrypto1.users.dto.ChangeUserNameDto;
import org.example.i2iacademyletcrypto1.users.dto.UserInfo;
import org.example.i2iacademyletcrypto1.wallets.WalletsRepo;
import org.example.i2iacademyletcrypto1.wallets.WalletsTable;
import org.example.i2iacademyletcrypto1.zcommon.exception.AlreadyExistException;
import org.example.i2iacademyletcrypto1.zcommon.exception.ResourceNotFoundException;
import org.example.i2iacademyletcrypto1.zcommon.exception.UpdateException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final SessionService sessionService;
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final WalletsRepo walletsRepo;

    @Transactional
    public UsersTable findUserById(String token) {
        // BURADA REDİS'E GİDİYORUZ:
        UUID userId = sessionService.getUserIdByToken(token.replace("Bearer ", ""));

        if (userId == null) {
            throw new ResourceNotFoundException("USER NOT FOUND EXCEPTİON"); // token Redis'te yok/süresi dolmuş
        }

        return usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public void changePassword(UsersTable usersTable, String newPassword, String oldPassword) {
        /*MATCH PASSWORD*/
        boolean matchPassword = passwordEncoder.matches(oldPassword, usersTable.getPassword());
        if (!matchPassword) {
            throw new ResourceNotFoundException("PASSWORD NOT FOUND");
        }
        /*hashed new password*/
        String hash = passwordEncoder.encode(newPassword);
        int updatePassword = usersRepo.updatePassword(hash, usersTable.getId(), usersTable.getVersion());
        if (updatePassword == 0) {
            throw new UpdateException("PASSWORD NOT UPDATED");
        }
    }

    @Transactional
    public void changeEmail(UsersTable usersTable, ChangeEmailDto changeEmailDto) {
        /*email exist control*/
        UsersTable findByEmail = usersRepo.findByEmail(changeEmailDto.getNewEmail());
        if (findByEmail != null) {
            throw new AlreadyExistException("EMAIL ALREADY EXIST");
        }
        boolean matchPassword = passwordEncoder.matches(changeEmailDto.getPassword(), usersTable.getPassword());
        if (!matchPassword) {
            throw new ResourceNotFoundException("PASSWORD NOT FOUND");
        }
        int updateEmail = usersRepo.updateEmail(changeEmailDto.getNewEmail(), usersTable.getId(), usersTable.getVersion(), changeEmailDto.getOldEmail());
        if (updateEmail == 0) {
            throw new UpdateException("EMAIL NOT UPDATED");
        }
    }

    @Transactional
    public void changeUserName(UsersTable usersTable, ChangeUserNameDto changeUserNameDto) {
        UsersTable findByUsername = usersRepo.findByUsername(changeUserNameDto.getNewUsername());

        if (findByUsername != null) {
            throw new AlreadyExistException("USERNAME ALREADY EXIST");
        }
        boolean matchPassword = passwordEncoder.matches(changeUserNameDto.getPassword(), usersTable.getPassword());
        if (!matchPassword) {
            throw new ResourceNotFoundException("PASSWORD NOT FOUND");
        }

        int updateUserName = usersRepo.updateUsername(changeUserNameDto.getNewUsername(), usersTable.getId(), usersTable.getVersion());

        if (updateUserName == 0) {
            throw new UpdateException("USERNAME NOT UPDATED");
        }
    }

    @Transactional(readOnly = true)
    public UserInfo userInfo(UsersTable usersTable) {
        WalletsTable findWallet = walletsRepo.findByUserId(usersTable.getId());
        if (findWallet == null) {
            throw new ResourceNotFoundException("WALLET NOT FOUND");
        }
        return UserInfo.builder()
                .email(usersTable.getEmail())
                .username(usersTable.getUsername())
                .cashBalance(findWallet.getCashBalance())
                .build();
    }
}

