package org.example.pay.global.service;

import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.UserErrorCode;
import org.example.pay.user.domain.User;
import org.example.pay.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new PayException(UserErrorCode.NOT_FOUND_USER));

        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities, user.getId());
    }
}