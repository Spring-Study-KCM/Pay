package org.example.pay.global.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendErrorCode implements DefaultErrorCode{

	//404 NOT_FOUND
	FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구 관계가 존재하지 않습니다"),
	;

	private HttpStatus httpStatus;
	private String message;
}
