package org.example.pay.global.dto;

public record ErrorDto(
        int code,
        String message
) {
    public static ErrorDto of(int code, String message) {
        return new ErrorDto(code, message);
    }
}
