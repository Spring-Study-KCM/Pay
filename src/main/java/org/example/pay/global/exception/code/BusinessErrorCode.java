package org.example.pay.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements DefaultErrorCode {

    //400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_REQUIRED_HEADER(HttpStatus.BAD_REQUEST,"필수 헤더가 누락되었습니다."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST,"필수 파라미터가 누락되었습니다."),
    MISSING_REQUIRED_COOKIE(HttpStatus.BAD_REQUEST, "필수 쿠키가 누락되었습니다."),

    // 500 INTERNAL_SEVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    ;

    private HttpStatus httpStatus;
    private String message;
}