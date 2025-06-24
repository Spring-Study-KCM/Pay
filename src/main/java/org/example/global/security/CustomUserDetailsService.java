package org.example.global.security;

import lombok.RequiredArgsConstructor;
import org.example.domain.user.entity.User;
import org.example.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // CustomUserPrincipal 사용 (User 객체 포함)
        return new CustomUserPrincipal(user);
    }
}
