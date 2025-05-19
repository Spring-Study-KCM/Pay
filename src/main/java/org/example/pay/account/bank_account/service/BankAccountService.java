package org.example.pay.account.bank_account.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.pay.account.bank_account.domain.BankAccount;
import org.example.pay.account.bank_account.dto.request.BankType;
import org.example.pay.account.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.account.bank_account.repository.BankAccountRepository;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.example.pay.account.pay_account.service.PayAccountService;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.BankAccountErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final PayAccountService payAccountService;

    public List<BankAccountInfoResponse> getConnectedBankAccount(Long userId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByUserId(userId);

        if (bankAccounts.isEmpty()) {
            throw new PayException(BankAccountErrorCode.NOT_FOUND_BANK);
        }

        return bankAccounts.stream()
                .map(bankAccount -> new BankAccountInfoResponse(
                        bankAccount.getBankType().getName(),
                        bankAccount.getAccountNumber(),
                        bankAccount.getBalance()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void connectBankAccount(Long userId, String bankName, String bankAccountNumber) {
        PayAccount payAccount = payAccountService.getPayAccount(userId);

        BankType bankType = validateBankName(bankName);

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(bankAccountNumber)
                .bankType(bankType)
                .payAccount(payAccount)
                .user(payAccount.getUser())
                .build();

        bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void disConnectBankAccount(Long bankId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankId)
                .orElseThrow(() -> new PayException(BankAccountErrorCode.NOT_FOUND_BANK_NAME));

        bankAccountRepository.delete(bankAccount);
    }

    @Transactional
    public void withdraw(BankAccount bankAccount, Long amount) {

        if (!validateBalance(bankAccount, amount)) {
            throw new PayException(BankAccountErrorCode.NOT_ENOUGH_MONEY);
        }

        bankAccount.withdraw(amount);
        bankAccountRepository.save(bankAccount);
    }

    public BankAccount getBankAccountWithPayAccount(Long bankId) {
        return bankAccountRepository.findByIdWithPayAccount(bankId)
                .orElseThrow(() -> new PayException(BankAccountErrorCode.NOT_FOUND_BANK_NAME));
    }

    private BankType validateBankName(String bankName) {
        return BankType.getBankType(bankName);
    }

    private boolean validateBalance(BankAccount bankAccount, Long amount) {
        if (bankAccount.getBalance() < amount) {
            return false;
        }
        return true;
    }

}