package org.example.pay.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.AuthErrorCode;
import org.example.pay.user.domain.User;
import org.example.pay.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final String VERIFICATION_CODE_STATUS = "verification:status:";

    @Transactional
    public void register(String email, String password) {

        boolean isVerified = redisTemplate.hasKey(VERIFICATION_CODE_STATUS + email) &&
                Boolean.TRUE.toString().equals(redisTemplate.opsForValue().get(VERIFICATION_CODE_STATUS + email));

        if (!isVerified) {
            throw new PayException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }

        if (userRepository.existsByEmail(email)) {
            throw new PayException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        redisTemplate.delete(VERIFICATION_CODE_STATUS + email);
    }
}
