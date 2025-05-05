package org.example.pay.bank_account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.pay.auth.dto.request.UserSessionId;
import org.example.pay.bank_account.dto.request.BackAccountRequest;
import org.example.pay.bank_account.dto.request.DisconnectAccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bank Account Controller", description = "페이 계좌 충전 및 금융 계좌 연동 관련 API 입니다.")
@RestController
@RequestMapping("/account/bank")
public class BankAccountController {

    @Operation(summary = "페이 계좌 연결 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 연결 성공입니다."),
    })
    @PostMapping("/connect")
    public ResponseEntity<Object> connectBankAccount(@RequestBody final BackAccountRequest backAccountRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "페이 연결된 금융 계좌 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "페이와 연결된 계좌 목록들입니다."),
    })
    @GetMapping("/connect")
    public ResponseEntity<Object> connectedBankAccount(@RequestBody final UserSessionId userSessionId) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
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
