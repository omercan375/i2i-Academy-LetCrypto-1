package org.example.i2iacademyletcrypto1.users;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.users.dto.ChangeEmailDto;
import org.example.i2iacademyletcrypto1.users.dto.ChangePasswordDto;
import org.example.i2iacademyletcrypto1.users.dto.ChangeUserNameDto;
import org.example.i2iacademyletcrypto1.users.dto.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-service")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;


    @PutMapping("/change-password")
    @Operation(summary = "change password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        UsersTable findUser = usersService.findUserById(token);
        usersService.changePassword(findUser, changePasswordDto.getNewPassword(),changePasswordDto.getOldPassword());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/change-email")
    @Operation(summary = "change email")
    public ResponseEntity<?> changeEmail(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangeEmailDto changeEmailDto) {
        UsersTable findUser = usersService.findUserById(token);
        usersService.changeEmail(findUser,changeEmailDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/change-username")
    @Operation(summary = "change username")
    public ResponseEntity<?> changeUserName(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangeUserNameDto changeUserNameDto) {
        UsersTable findUser = usersService.findUserById(token);
        usersService.changeUserName(findUser,changeUserNameDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/info")
    @Operation(summary = "get user infos")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        UsersTable findUser = usersService.findUserById(token);
        UserInfo userInfo = usersService.userInfo(findUser);
        return ResponseEntity.ok(userInfo);
    }



}
