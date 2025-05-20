package org.example.pay.wallet.dto.response;

import org.example.pay.wallet.domain.Wallet;

public record WalletResponseDto(
	String uuid,
	Long balance
) {
	public static WalletResponseDto of(Wallet wallet) {
		return new WalletResponseDto(wallet.getUuid(), wallet.getBalance());
	}
}
