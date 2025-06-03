package org.example.repository;

import org.example.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    @Query("""
    SELECT c FROM Charge c 
    JOIN FETCH c.wallet w 
    JOIN FETCH c.realAccount r 
    WHERE w.id = :walletId
""")
    List<Charge> findByWalletIdWithFetch(@Param("walletId") Long walletId);

    @Query("""
    SELECT c FROM Charge c 
    JOIN FETCH c.wallet w 
    JOIN FETCH c.realAccount r 
    WHERE w.id = :walletId 
    AND c.chargedAt BETWEEN :from AND :to
""")
    List<Charge> findByWalletIdAndChargedAtBetweenWithFetch(
            @Param("walletId") Long walletId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
