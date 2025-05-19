package org.example.pay.account.bank_account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.pay.account.bank_account.dto.request.BankType;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.example.pay.user.domain.User;

@Getter
@Table(name = "BANK_ACCOUNT")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_account_id")
    private PayAccount payAccount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "bank_type")
    private BankType bankType;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private Long balance = 100000L;

    @Builder
    public BankAccount(String accountNumber, BankType bankType, PayAccount payAccount, User user) {
        this.accountNumber = accountNumber;
        this.bankType = bankType;
        this.payAccount = payAccount;
        this.user = user;
    }

    public void withdraw(Long amount) {
        this.balance -= amount;
    }
}
