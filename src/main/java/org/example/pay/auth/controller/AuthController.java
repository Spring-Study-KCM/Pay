package org.example.pay.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.pay.auth.dto.request.EmailVerificationCodeVerifyRequest;
import org.example.pay.auth.dto.request.LoginRequest;
import org.example.pay.auth.dto.request.RegisterRequest;
import org.example.pay.auth.dto.request.EmailVerificationCodeSendRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "로그인 및 회원 가입 관련 API 입니다.")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Operation(summary = "페이 이메일 전송 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 코드를 발송했습니다."),
    })
    @PostMapping("/email/verification-code/send")
    public ResponseEntity<Object> sendVerificationCode(@RequestBody final EmailVerificationCodeSendRequest emailVerificationCodeSendRequestr) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "페이 이메일 인증 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "이메일 인증 성공입니다."),
    })
    @PostMapping("/email/verification-code/verify")
    public ResponseEntity<Object> sendVerificationCode(@RequestBody final EmailVerificationCodeVerifyRequest emailVerificationCodeVerifyRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "페이 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원가입 성공입니다."),
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody final RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "페이 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그인 성공입니다."),
    })
    @PostMapping("/login")
    public ResponseEntity<Object> register(@RequestBody final LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
