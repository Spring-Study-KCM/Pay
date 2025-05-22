package org.example.repository;

import org.example.entity.RealAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealAccountRepository extends JpaRepository<RealAccount, Long> {
    List<RealAccount> findAllByUserId(Long userId);
}
