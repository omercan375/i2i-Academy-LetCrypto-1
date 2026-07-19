package org.example.i2iacademyletcrypto1.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UsersRepo extends CrudRepository<UsersTable, UUID> {
    @Query("SELECT u FROM UsersTable u WHERE u.email=:email")
    UsersTable findByEmail(@Param("email") String email);

    @Query("SELECT u FROM UsersTable u WHERE u.username=:username")
    UsersTable findByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE UsersTable u SET u.password=:newPassword, u.version=u.version+1 where u.id=:userId and u.version=:version")
    int updatePassword(@Param("newPassword")String newPassword, @Param("userId")UUID userId, @Param("version")int version);

    @Modifying
    @Query("UPDATE UsersTable u SET u.email=:email,u.version=u.version+1 WHERE u.id=:userId and u.version=:version and u.email=:oldEmail")
    int updateEmail(@Param("email")String email,@Param("userId") UUID userId ,@Param("version")int version, @Param("oldEmail") String oldEmail);

    @Modifying
    @Query("UPDATE UsersTable u SET u.username=:newUsername,u.version=u.version+1 WHERE u.id=:userId  and u.version=:version")
    int updateUsername(@Param("newUsername")String newUsername, @Param("userId") UUID userId,@Param("version")int version);
}
