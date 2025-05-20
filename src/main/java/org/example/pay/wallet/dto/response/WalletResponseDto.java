package org.example.pay.wallet.dto.response;

import org.example.pay.wallet.domain.Wallet;

import io.swagger.v3.oas.annotations.media.Schema;

public record WalletResponseDto(
	@Schema(description = "지갑 uuid", example = "uuid")
	String uuid,
	@Schema(description = "잔액", example = "13000")
	Long balance
) {
	public static WalletResponseDto of(Wallet wallet) {
		return new WalletResponseDto(wallet.getUuid(), wallet.getBalance());
	}
}
