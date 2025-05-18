package org.example.pay.pay_account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pay.pay_account.domain.PayAccount;
import org.example.pay.pay_account.repository.PayAccountRepository;
import org.example.pay.user.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayAccountService {

    private final PayAccountRepository payAccountRepository;

    @Transactional
    public void createPayAccount(User user) {
        PayAccount payAccount = PayAccount.builder()
                .user(user)
                .build();

        payAccountRepository.save(payAccount);
    }


}
