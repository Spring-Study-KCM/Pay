package org.example.pay.account.pay_account.service;

import lombok.RequiredArgsConstructor;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.example.pay.account.pay_account.repository.PayAccountRepository;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.PayAccountErrorCode;
import org.example.pay.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayAccountService {

    private final PayAccountRepository payAccountRepository;

    @Transactional
    public void createPayAccount(User user) {
        PayAccount payAccount = PayAccount.builder()
                .user(user)
                .build();

        payAccountRepository.save(payAccount);
    }

    public PayAccount getPayAccount(Long userId) {
        return payAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new PayException(PayAccountErrorCode.NOT_FOUND_PAY_ACCOUNT));
    }

    @Transactional
    public void deposit(PayAccount payAccount, Long amount) {
        payAccount.deposit(amount);
        payAccountRepository.save(payAccount);
    }
}