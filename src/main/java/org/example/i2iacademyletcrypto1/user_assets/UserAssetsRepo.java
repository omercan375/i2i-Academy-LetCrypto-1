package org.example.i2iacademyletcrypto1.user_assets;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface UserAssetsRepo extends CrudRepository<UserAssetsTable, UUID> {
    @Query("SELECT u FROM UserAssetsTable u where u.user.id=:userId")
    List<UserAssetsTable> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT u FROM UserAssetsTable u " +
            "JOIN FETCH u.user " +
            "JOIN FETCH u.asset " +
            "WHERE u.user.id = :userId AND u.asset.id = :assetId")
    UserAssetsTable findUserAssets(@Param("userId") UUID userId,
                                   @Param("assetId") UUID assetId);


    @Modifying
    @Query("UPDATE UserAssetsTable u " +
            "SET u.quantity = :newQuantity, u.version = u.version + 1 " +
            "WHERE u.id = :id AND u.version = :version")
    int updateQuantity(@Param("newQuantity") BigDecimal newQuantity,
                       @Param("id") UUID id,
                       @Param("version") Integer version);

    @Modifying
    @Query("DELETE FROM UserAssetsTable u WHERE u.id=:id")
    void deleteUserAssets(@Param("id") UUID id);
}
