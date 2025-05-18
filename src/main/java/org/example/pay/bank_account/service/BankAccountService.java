package org.example.pay.bank_account.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.pay.bank_account.domain.BankAccount;
import org.example.pay.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.bank_account.repository.BankAccountRepository;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.BankAccountErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

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

}
