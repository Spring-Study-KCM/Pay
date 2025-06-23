package org.example.pay.account.pay_account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.pay.account.pay_account.dto.request.DepositRequest;
import org.example.pay.account.pay_account.dto.request.ReloadAccountRequest;
import org.example.pay.account.pay_account.dto.request.RemitRequest;
import org.example.pay.account.pay_account.dto.request.TransactionsInfoRequest;
import org.example.pay.account.pay_account.dto.response.PayType;
import org.example.pay.account.pay_account.dto.response.TransactionsInfoListResponse;
import org.example.pay.account.service.AccountService;
import org.example.pay.global.dto.CursorPageRequest;
import org.example.pay.global.dto.ResponseDto;
import org.example.pay.global.service.CustomUserDetails;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pay Account Controller", description = "페이 계좌 충전 송금 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/pay")
public class PayAccountController {

	private final AccountService accountService;

	@Operation(summary = "페이 계좌 충전 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "페이에 금액을 충전했습니다."),
	})
	@PostMapping("/reload")
	public ResponseEntity<ResponseDto<Void>> reloadAccount(
		@RequestBody final ReloadAccountRequest reloadAccountRequest) {

		accountService.reloadAccount(reloadAccountRequest.bankId(), reloadAccountRequest.depositAmount());

		return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success());
	}

	@Operation(summary = "페이 송금 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "송금을 완료했습니다."),
	})
	@PostMapping("/remit")
	public ResponseEntity<Object> remit(@RequestBody final RemitRequest remitRequest, @AuthenticationPrincipal
	CustomUserDetails userDetails) {

		accountService.remit(remitRequest.friendId(), remitRequest.amount(), userDetails.getId());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@Operation(summary = "페이 입금 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "입금을 완료했습니다."),
	})
	@PostMapping("/deposit")
	public ResponseEntity<Object> deposit(@RequestBody final DepositRequest depositRequest, @AuthenticationPrincipal
	CustomUserDetails userDetails) {

		accountService.deposit(depositRequest.transactionsId(), depositRequest.friendId(), depositRequest.amount(),
			userDetails.getId());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@Operation(summary = "거래 내역 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "입금을 완료했습니다."),
	})
	@GetMapping()
	public ResponseEntity<Object> getTransactionsList(
		@ParameterObject @Valid CursorPageRequest request,
		@RequestBody final TransactionsInfoRequest transactionsInfoRequest,
		@RequestParam(value = "type", required = false) PayType payType, @AuthenticationPrincipal
		CustomUserDetails userDetails) {

		TransactionsInfoListResponse transactionsList = accountService.getTransactionsList(request,
			transactionsInfoRequest.payAccountId(), payType);
		return ResponseEntity.status(HttpStatus.OK).body(transactionsList);
	}
}
