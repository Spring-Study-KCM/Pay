package org.example.pay.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.pay.global.dto.ResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 인증 성공 시 ResponseDto 형식의 JSON 응답을 반환하는 핸들러
 */
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler implements
        org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        session.setAttribute("CURRENT_USER", authentication.getName());

        Map<String, Object> data = new HashMap<>();
        data.put("message", "로그인 성공");
        data.put("username", authentication.getName());
        data.put("roles", roles);
        data.put("sessionId", sessionId);

        ResponseDto<Map<String, Object>> responseDto = ResponseDto.success(data);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}