package org.example.pay.wallet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChargeRequestDto(
	@Schema(description = "계좌 ID", example = "1")
	Long accountId,
	@Schema(description = "금액", example = "10000")
	Long amount
) {
}
