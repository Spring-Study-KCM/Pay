package org.example.pay.auth.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutHandler implements LogoutSuccessHandler {

	private final ObjectMapper objectMapper;
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		request.getSession(false);
		if (request.getSession(false) != null) {
			request.getSession(false).invalidate();
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> body = new HashMap<>();
		body.put("message", "로그아웃 성공");

		response.getWriter().write(objectMapper.writeValueAsString(body));

	}
}
