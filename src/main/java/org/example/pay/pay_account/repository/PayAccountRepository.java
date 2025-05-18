package org.example.pay.pay_account.repository;

import org.example.pay.pay_account.domain.PayAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayAccountRepository extends JpaRepository<PayAccount, Long> {
}
