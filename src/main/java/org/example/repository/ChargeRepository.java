package org.example.repository;

import org.example.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByWalletIdAndChargedAtBetween(Long walletId, LocalDateTime from, LocalDateTime to);
    List<Charge> findByWalletId(Long walletId);
}
