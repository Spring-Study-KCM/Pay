package org.example.pay.wallet.dto.request;

public record ChargeRequestDto(
	Long accountId,
	Long amount
) {
}
