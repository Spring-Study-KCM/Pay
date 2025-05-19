package org.example.pay.auth.converter;

import java.io.IOException;

import org.example.pay.auth.dto.request.LoginRequestDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JsonAuthenticationConverter implements AuthenticationConverter {

	private final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public Authentication convert(HttpServletRequest request) {
		try {
			LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
			return new UsernamePasswordAuthenticationToken(
				requestDto.email(), requestDto.password()
			);
		} catch (IOException e) {
			throw new RuntimeException("로그인 요청 파싱 실패");
		}

	}
}
