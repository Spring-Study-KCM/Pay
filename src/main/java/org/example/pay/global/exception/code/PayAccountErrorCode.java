package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PayAccountErrorCode implements DefaultErrorCode{

    NOT_FOUND_PAY_ACCOUNT(HttpStatus.NOT_FOUND, "연결된 페이 계좌가 없습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
