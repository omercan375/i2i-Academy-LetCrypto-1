package org.example.i2iacademyletcrypto1.user_assets.abstract_class;

import org.example.i2iacademyletcrypto1.assets.AssetsTable;
import org.example.i2iacademyletcrypto1.user_assets.dto.PortfolioDto;
import org.example.i2iacademyletcrypto1.users.UsersTable;

import java.math.BigDecimal;
import java.util.List;

public interface AbstractUserAssetsService {
    public List<PortfolioDto> portfolio(UsersTable user);
    public void buyAsset(UsersTable user, AssetsTable asset, BigDecimal quantity);
    public void sellAsset(UsersTable user, AssetsTable asset,BigDecimal quantity);
    public void sellAllAsset(UsersTable user,AssetsTable asset,BigDecimal quantity);
}

