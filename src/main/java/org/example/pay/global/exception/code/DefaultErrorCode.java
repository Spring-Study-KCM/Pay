package org.example.pay.global.exception.code;

import org.springframework.http.HttpStatus;

public interface DefaultErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
}
