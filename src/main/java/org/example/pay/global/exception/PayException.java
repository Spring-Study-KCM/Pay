package org.example.pay.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.pay.global.exception.code.DefaultErrorCode;

@Getter
@AllArgsConstructor
public class PayException extends RuntimeException {
    private final DefaultErrorCode errorCode;
}
