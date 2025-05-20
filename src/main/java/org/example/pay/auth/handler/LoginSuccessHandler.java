package org.example.pay.auth.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.example.pay.auth.service.SecurityContextService;
import org.example.pay.member.domain.Member;
import org.example.pay.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;
	private final MemberService memberService;
	private final SecurityContextService securityContextService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String email = authentication.getName();
		Member member = memberService.getByEmail(email);

		HttpSession session = request.getSession(true);
		securityContextService.saveSecurityContext(session, member);

		makeResponse(response, session);
	}

	private void makeResponse(HttpServletResponse response, HttpSession session) throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put("message", "로그인 성공");
		data.put("sessionId", session.getId());

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(data));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
