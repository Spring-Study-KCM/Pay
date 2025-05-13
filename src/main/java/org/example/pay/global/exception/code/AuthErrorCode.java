package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements DefaultErrorCode{

    //400 BAD_REQUEST
    VERIFICATION_INFO_NULL(HttpStatus.BAD_REQUEST, "인증 정보가 없습니다."),
    VERIFICATION_CODE_WRONG(HttpStatus.BAD_REQUEST, "인증번호가 틀렸습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),

    // 401 UNAUTHORIZED
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다."),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
