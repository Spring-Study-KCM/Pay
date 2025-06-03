package org.example.repository;

import org.example.entity.RealAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RealAccountRepository extends JpaRepository<RealAccount, Long> {
    @Query("SELECT r FROM RealAccount r JOIN FETCH r.user WHERE r.user.id = :userId")
    List<RealAccount> findAllByUserIdFetchJoin(@Param("userId") Long userId);
}
