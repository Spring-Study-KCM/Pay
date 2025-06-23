package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PayAccountErrorCode implements DefaultErrorCode{

    // 400
    NOT_FOUND_PAY_ACCOUNT(HttpStatus.NOT_FOUND, "연결된 페이 계좌가 없습니다."),
    NOT_ENOUGH_PAY_BALANCE(HttpStatus.BAD_REQUEST, "잔액 부족입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
