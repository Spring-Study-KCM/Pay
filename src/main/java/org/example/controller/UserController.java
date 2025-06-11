package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AuthRequest;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.entity.UserResponseDto;
import org.example.security.CustomUserPrincipal;
import org.example.security.JwtProvider;
import org.example.service.MailService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody @Valid UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        // 1. 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Principal에서 CustomUserPrincipal 가져오기
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        // 3. user 정보를 사용하여 토큰 생성 (id, email, role 포함)
        String token = jwtProvider.generateToken(user.getId(), user.getEmail());

        // 4. 토큰 응답
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // JWT 기반 로그아웃은 일반적으로 서버가 상태를 갖지 않음
        // → 클라이언트가 토큰을 지우면 로그아웃 처리 완료
        // → 보안을 강화하고 싶다면 블랙리스트를 도입

        // 요청 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");

        // 클라이언트가 헤더를 제거하도록 안내
        if (token != null && token.startsWith("Bearer ")) {
            return ResponseEntity.ok("JWT 로그아웃 완료 - 클라이언트에서 토큰 제거 필요");
        } else {
            return ResponseEntity.badRequest().body("Authorization 헤더가 없습니다.");
        }
    }


    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam String email) {
        mailService.sendVerificationMail(email);
        return ResponseEntity.ok("인증 메일이 전송되었습니다.");
    }


    @GetMapping
    public ResponseEntity<UserResponseDto> getMyInfo(Authentication authentication) {
        // Authentication에서 User 직접 추출
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        UserResponseDto responseDto = new UserResponseDto(user.getId(), user.getEmail());
        return ResponseEntity.ok(responseDto);
    }
}
