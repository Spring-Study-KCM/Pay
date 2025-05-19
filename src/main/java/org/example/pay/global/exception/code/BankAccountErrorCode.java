package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BankAccountErrorCode implements DefaultErrorCode {

    //400
    BANK_NAME_NOT_NULL(HttpStatus.BAD_REQUEST, "금융기관은 null일 수 없습니다."),
    NOT_FOUND_BANK_NAME(HttpStatus.NOT_FOUND, "해당 금융기관은 존재하지 않습니다."),
    NOT_FOUND_BANK(HttpStatus.NOT_FOUND, "연결된 계좌가 없습니다."),
    NOT_ENOUGH_MONEY(HttpStatus.BAD_REQUEST, "잔액 부족입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
