package org.example.pay.auth.dto.request;

public record EmailVerificationCodeVerifyRequest(String email, String code) {
}
