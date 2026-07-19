package org.example.i2iacademyletcrypto1.user_assets;

import lombok.RequiredArgsConstructor;
import org.example.i2iacademyletcrypto1.user_assets.abstract_class.AbstractUserAssetsController;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.example.i2iacademyletcrypto1.users.UsersService;
import org.example.i2iacademyletcrypto1.users.UsersTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-assets")
@RequiredArgsConstructor
public class UserAssetsController implements AbstractUserAssetsController {

    private final UsersService usersService;
    private final UserAssetsService userAssetsService;


    @GetMapping("/portfolio")
    @Override
    public ResponseEntity<List<PortfolioDto>> getPortfolio(@RequestHeader("Authorization") String token){
        UsersTable findUser = usersService.findUserById(token);
        List<PortfolioDto> portfolio = userAssetsService.portfolio(findUser);
        return ResponseEntity.ok(portfolio);
    }
}
