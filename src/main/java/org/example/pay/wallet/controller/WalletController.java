package org.example.pay.wallet.controller;

import org.example.pay.auth.domain.CustomUserDetails;
import org.example.pay.wallet.dto.request.ChargeRequestDto;
import org.example.pay.wallet.dto.response.WalletResponseDto;
import org.example.pay.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "지갑 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {

	private final WalletService walletService;

	@GetMapping
	@Operation(summary = "페이머니 상세 조회 API",
		description = "페이머니 상세 조회",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<WalletResponseDto> getWallet(@AuthenticationPrincipal CustomUserDetails principal) {
		WalletResponseDto response =  walletService.getWallet(principal.getId());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Operation(summary = "지갑 추가 API",
		description = "지갑 추가",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<WalletResponseDto> createWallet(@AuthenticationPrincipal CustomUserDetails principal) {
		walletService.createWallet(principal.getId());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/charge")
	@Operation(summary = "페이머니 충전 API",
		description = "페이머니 충전",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<Object> chargePayMoney(@RequestBody ChargeRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails principal) {
		walletService.chargePayMoney(requestDto, principal.getId());
		return ResponseEntity.ok().build();
	}
}
