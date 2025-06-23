package org.example.pay.account.pay_account.dto.response;

import org.example.pay.global.dto.CursorPage;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionsInfoListResponse(
	@Schema(description = "총 거래 내역 수", example = "24")
	int transactionsCounts,

	CursorPage<TransactionsInfoResponse> transactionsInfoResponseCursorPage
) {
	public static TransactionsInfoListResponse of(int transactionsCounts, CursorPage<TransactionsInfoResponse> transactionsInfoResponseCursorPage) {
		return new TransactionsInfoListResponse(transactionsCounts, transactionsInfoResponseCursorPage);
	}
}
