package org.example.repository;

import org.example.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
//    Optional<Wallet> findByUserId(Long userId);
    @Query("select w from Wallet w join fetch w.user where w.user.id = :userId")
    Optional<Wallet> findByUserIdFetchJoin(@Param("userId") Long userId);
}
