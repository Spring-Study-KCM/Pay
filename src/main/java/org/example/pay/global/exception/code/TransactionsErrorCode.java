package org.example.pay.global.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionsErrorCode implements DefaultErrorCode{

	//400 BAD_REQUEST
	NOT_FOUND_TRANSACTIONS(HttpStatus.NOT_FOUND, "거래내역을 찾을 수 없습니다."),
	;

	private HttpStatus httpStatus;
	private String message;
}
