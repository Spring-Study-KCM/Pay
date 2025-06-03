package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // User → UserDetails 변환
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()  // 권한이 없을 경우
        );
    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmailFetchJoin(email)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
//        return new CustomUserPrincipal(user); // 👈 User 대신 CustomUserPrincipal 사용
//    }
}
