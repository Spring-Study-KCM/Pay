package org.example.pay.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record JoinRequestDto(
	@Schema(description = "이메일", example = "test@naver.com")
	String email,
	@Schema(description = "이름", example = "cherry")
	String name,
	@Schema(description = "비밀번호", example = "password")
	String password
) {

}
