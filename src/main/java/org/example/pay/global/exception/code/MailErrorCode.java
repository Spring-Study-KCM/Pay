package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MailErrorCode implements DefaultErrorCode{

    //400 BAD_REQUEST
    MISSING_EMAIL(HttpStatus.BAD_REQUEST, "이메일 주소가 누락되었습니다."),

    //500 INTERNAL_SERVER_ERROR
    EMAIL_SENDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}
