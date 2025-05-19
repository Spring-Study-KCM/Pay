package org.example.pay.bank_account.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.pay.bank_account.domain.BankAccount;
import org.example.pay.bank_account.dto.request.BankType;
import org.example.pay.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.bank_account.repository.BankAccountRepository;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.BankAccountErrorCode;
import org.example.pay.pay_account.domain.PayAccount;
import org.example.pay.pay_account.service.PayAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final PayAccountService payAccountService;

    public List<BankAccountInfoResponse> getConnectedBankAccount(Long id) {

        List<BankAccount> bankAccounts = bankAccountRepository.findByUserId(id);

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

    private BankType validateBankName(String bankName) {
        return BankType.getBankType(bankName);
    }
}
