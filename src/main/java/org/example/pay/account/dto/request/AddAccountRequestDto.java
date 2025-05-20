package org.example.pay.account.dto.request;

import org.example.pay.account.constants.BankCode;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddAccountRequestDto(
	@Schema(description = "은행 이름", example = "KOOKMIN")
	BankCode bankCode,
	@Schema(description = "계좌 번호", example = "12345-1234")
	String accountNumber,
	@Schema(description = "별칭", example = "주거래 계좌")
	String name,
	@Schema(description = "디폴트 계좌 여부", example = "true")
	boolean isDefault
) {
}
