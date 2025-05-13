package org.example.pay.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.pay.global.dto.ResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler implements
        org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {

        Map<String, Object> data = new HashMap<>();
        data.put("message", "로그아웃 성공");
        
        ResponseDto<Map<String, Object>> responseDto = ResponseDto.success(data);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}