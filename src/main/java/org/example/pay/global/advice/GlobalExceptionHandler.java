package org.example.pay.global.advice;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.pay.global.dto.ResponseDto;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.BusinessErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {PayException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleBusinessException(PayException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.validFail(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleMissingHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(BusinessErrorCode.MISSING_REQUIRED_HEADER.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_HEADER));
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseDto<String>> handleMissingParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_PARAMETER));
    }

    @ExceptionHandler(value = {MissingRequestCookieException.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleMissingCookieException(MissingRequestCookieException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.fail(BusinessErrorCode.MISSING_REQUIRED_COOKIE));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDto<BusinessErrorCode>> handleException(Exception e) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(BusinessErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ResponseDto.fail(BusinessErrorCode.INTERNAL_SERVER_ERROR));
    }
}