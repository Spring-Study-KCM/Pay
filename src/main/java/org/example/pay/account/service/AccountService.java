package org.example.pay.account.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.pay.account.bank_account.domain.BankAccount;
import org.example.pay.account.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.account.bank_account.service.BankAccountService;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.example.pay.account.pay_account.service.PayAccountService;
import org.example.pay.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final PayAccountService payAccountService;
    private final BankAccountService bankAccountService;

    @Transactional
    public void reloadAccount(Long bankId, Long depositAmount) {
        BankAccount bankAccount = bankAccountService.getBankAccountWithPayAccount(bankId);
        PayAccount payAccount = bankAccount.getPayAccount();

        bankAccountService.withdraw(bankAccount, depositAmount);
        payAccountService.deposit(payAccount, depositAmount);
    }

    @Transactional
    public void connectBankAccount(Long userId, String bankName, String bankAccountNumber) {
        bankAccountService.connectBankAccount(userId, bankName, bankAccountNumber);
    }

    @Transactional
    public void disconnectBankAccount(Long bankId) {
        bankAccountService.disConnectBankAccount(bankId);
    }

    public List<BankAccountInfoResponse> getConnectedBankAccount(Long userId) {
        return bankAccountService.getConnectedBankAccount(userId);
    }

    @Transactional
    public void createPayAccount(User user) {
        payAccountService.createPayAccount(user);
    }
}