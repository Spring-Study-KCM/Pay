package org.example.pay.account.bank_account.repository;

import java.util.List;
import java.util.Optional;
import org.example.pay.account.bank_account.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUserId(Long id);

    @Query("SELECT b FROM BankAccount b JOIN FETCH b.payAccount WHERE b.id = :bankId")
    Optional<BankAccount> findByIdWithPayAccount(@Param("bankId") Long bankId);
}
