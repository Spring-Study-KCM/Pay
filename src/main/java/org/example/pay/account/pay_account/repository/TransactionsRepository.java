package org.example.pay.account.pay_account.repository;

import org.example.pay.account.pay_account.domain.Transactions;
import org.example.pay.account.pay_account.repository.custom.TransactionsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions, Long>, TransactionsRepositoryCustom {
}
