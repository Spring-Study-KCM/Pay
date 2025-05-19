package org.example.pay.account.pay_account.repository;

import java.util.Optional;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayAccountRepository extends JpaRepository<PayAccount, Long> {

    Optional<PayAccount> findByUserId(Long userId);
}
