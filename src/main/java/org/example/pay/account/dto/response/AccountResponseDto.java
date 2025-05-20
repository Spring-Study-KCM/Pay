package org.example.pay.account.dto.response;

import org.example.pay.account.domain.Account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AccountResponseDto(
	@Schema(description = "계좌 uuid", example = "uuid")
	String uuid,
	@Schema(description = "은행 이름", example = "국민은행")
	String bank,
	@Schema(description = "별칭", example = "주거래 계좌")
	String name,
	@Schema(description = "계좌 번호", example = "12345-1234")
	String accountNumber,
	@Schema(description = "디폴트 계좌 여부", example = "true")
	boolean isDefault
) {
	public static AccountResponseDto of(Account account) {
		return AccountResponseDto.builder()
			.uuid(account.getUuid())
			.bank(account.getBankCode().getName())
			.name(account.getName())
			.accountNumber(account.getAccountNumber())
			.isDefault(account.isDefault())
			.build();
	}
}
