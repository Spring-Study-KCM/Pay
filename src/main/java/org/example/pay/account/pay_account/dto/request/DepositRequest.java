package org.example.pay.account.pay_account.dto.request;

public record DepositRequest(Long transactionsId, Long friendId, Long amount) {
}
