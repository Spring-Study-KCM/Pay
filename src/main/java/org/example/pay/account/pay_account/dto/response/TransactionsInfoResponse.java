package org.example.pay.account.pay_account.dto.response;

public record TransactionsInfoResponse(
	Long transactionsId,
	Long payAccountId,
	Long friendId,
	PayType payType
) {
}
