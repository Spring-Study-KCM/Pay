package org.example.pay.account.dto.request;

import org.example.pay.account.constants.BankCode;

public record AddAccountRequestDto(
	BankCode bankCode,
	String accountNumber,
	String name,
	boolean isDefault
) {
}
