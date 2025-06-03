package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    public User register(UserDto dto) {
        if (userRepository.findByEmailFetchJoin(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);
        walletService.createWallet(savedUser);

        return savedUser;
    }
}
