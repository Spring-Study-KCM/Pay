package org.example.pay.account.controller;

import java.util.List;

import org.example.pay.account.dto.request.AddAccountRequestDto;
import org.example.pay.account.dto.response.AccountResponseDto;
import org.example.pay.account.service.AccountService;
import org.example.pay.auth.domain.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "계좌 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	@Operation(summary = "계좌 추가 API",
		description = "계좌 추가 - 3개까지 가능",
		security = {@SecurityRequirement(name = "session")})
	@PostMapping
	public ResponseEntity<Object> addAccounts(@RequestBody AddAccountRequestDto accountRequestDto,
		@AuthenticationPrincipal CustomUserDetails principal) {
		accountService.addAccount(accountRequestDto, principal.getId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "계좌 목록 조회 API",
		description = "계좌 목록 조회",
		security = {@SecurityRequirement(name = "session")})
	@GetMapping
	public ResponseEntity<List<AccountResponseDto>> getAccounts(@AuthenticationPrincipal CustomUserDetails principal) {
		List<AccountResponseDto> response = accountService.getAccounts(principal.getId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "계좌 삭제 API",
		description = "계좌 삭제",
		security = {@SecurityRequirement(name = "session")})
	@DeleteMapping("/{uuid}")
	public ResponseEntity<Object> deleteAccounts(@PathVariable String uuid,
		@AuthenticationPrincipal CustomUserDetails principal) {
		accountService.deleteAccount(principal.getId(), uuid);
		return ResponseEntity.ok().build();
	}
}
