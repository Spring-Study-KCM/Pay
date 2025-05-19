package org.example.pay.bank_account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.pay.bank_account.dto.request.BackAccountRequest;
import org.example.pay.bank_account.dto.request.DisconnectAccountRequest;
import org.example.pay.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.bank_account.service.BankAccountService;
import org.example.pay.global.dto.ResponseDto;
import org.example.pay.global.service.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bank Account Controller", description = "페이 계좌 충전 및 금융 계좌 연동 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Operation(summary = "페이 계좌 연결 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 연결 성공입니다."),
    })
    @PostMapping("/connect")
    public ResponseEntity<ResponseDto<Void>> connectBankAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestBody final BackAccountRequest backAccountRequest) {

        bankAccountService.connectBankAccount(customUserDetails.getId(), backAccountRequest.bankName(),
                backAccountRequest.bankAccount());

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success());
    }

    @Operation(summary = "페이 연결된 금융 계좌 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "페이와 연결된 계좌 목록들입니다."),
    })
    @GetMapping("/connect")
    public ResponseEntity<ResponseDto<List<BankAccountInfoResponse>>> connectedBankAccount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<BankAccountInfoResponse> connectedBankAccount = bankAccountService.getConnectedBankAccount(
                customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(connectedBankAccount));
    }

    @Operation(summary = "페이와 연결된 금융 계좌 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "페이와 연결된 계좌를 삭제했습니다."),
    })
    @DeleteMapping("/connect")
    public ResponseEntity<Object> disconnectAccount(@RequestBody final DisconnectAccountRequest disconnectAccountRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
