package org.example.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.user.dto.UserDto;
import org.example.domain.user.entity.User;
import org.example.domain.user.repository.UserRepository;
import org.example.domain.wallet.service.WalletService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Transactional // 트랜잭션 추가
    public User register(UserDto dto) {
        // 이메일 중복 검사
        if (userRepository.findByEmailFetchJoin(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);

        // 회원가입과 동시에 지갑 생성 (같은 트랜잭션에서 처리)
        walletService.createWallet(savedUser);

        return savedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }


//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final WalletService walletService;
//
//    public User register(UserDto dto) {
//        if (userRepository.findByEmailFetchJoin(dto.getEmail()).isPresent()) {
//            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
//        }
//        User user = new User();
//        user.setName(dto.getName());
//        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        User savedUser = userRepository.save(user);
//        walletService.createWallet(savedUser);
//
//        return savedUser;
//    }
}
