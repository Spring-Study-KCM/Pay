package org.example.pay.auth.controller;

import org.example.pay.auth.dto.request.LoginRequestDto;
import org.example.pay.auth.service.AuthService;
import org.example.pay.auth.dto.request.JoinRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth API")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	@Operation(summary = "회원가입 API",
		description = "회원가입")
	public void join(@RequestBody JoinRequestDto joinRequestDto) {
		authService.join(joinRequestDto);
	}

	@Operation(summary = "개발용 세션 발급 API",
		description = """
			## 테스트용
			인증이 필요한 API 호출을 위해 유저 세션을 발급할 수 있는 API
			""")
	@PostMapping("/temp-login")
	public ResponseEntity<String> tempLogin(@RequestBody LoginRequestDto tempLoginDto, HttpServletRequest request) {
		String session = authService.tempLogin(tempLoginDto, request);
		return ResponseEntity.ok(session);
	}
}
