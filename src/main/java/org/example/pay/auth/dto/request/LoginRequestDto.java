package org.example.pay.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDto(
	@Schema(description = "이메일", example = "test@naver.com")
	String email,
	@Schema(description = "비밀번호", example = "password")
	String password
) {
}
