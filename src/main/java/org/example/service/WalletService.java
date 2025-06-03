package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.entity.Wallet;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWalletByUser(User user) {
        // User 객체에 Wallet이 이미 로드되어 있는지 확인
        if (user.getWallet() != null) {
            return user.getWallet();
        }

        // 없으면 DB에서 조회
        return walletRepository.findByUserIdFetchJoin(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("지갑이 존재하지 않습니다."));
    }

    public Wallet createWallet(User user) {
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(0L)
                .build();
        return walletRepository.save(wallet);
    }
}
