package org.example.pay.account.repository;

import java.util.Optional;

import org.example.pay.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUuid(String uuid);
}
