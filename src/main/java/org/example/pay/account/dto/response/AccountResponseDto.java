package org.example.pay.account.dto.response;

import org.example.pay.account.domain.Account;

import lombok.Builder;

@Builder
public record AccountResponseDto(
	String bank,
	String name,
	String accountNumber
) {
	public static AccountResponseDto of(Account account) {
		return AccountResponseDto.builder()
			.bank(account.getBankCode().getName())
			.name(account.getName())
			.accountNumber(account.getAccountNumber())
			.build();
	}
}
