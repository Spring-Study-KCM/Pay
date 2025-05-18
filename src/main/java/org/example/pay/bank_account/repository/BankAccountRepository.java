package org.example.pay.bank_account.repository;

import java.util.List;
import org.example.pay.bank_account.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUserId(Long id);
}
