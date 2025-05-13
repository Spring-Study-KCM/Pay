package org.example.pay.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String CREDENTIALS_EMAIL = "email";
    private static final String CREDENTIALS_PW = "password";

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
            String username = credentials.get(CREDENTIALS_EMAIL);
            String password = credentials.get(CREDENTIALS_PW);
            
            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);
            
            setDetails(request, authRequest);
            
            return this.getAuthenticationManager().authenticate(authRequest);
            
        } catch (IOException e) {
            throw new RuntimeException("인증 요청을 처리하는 중 오류가 발생했습니다", e);
        }
    }
}