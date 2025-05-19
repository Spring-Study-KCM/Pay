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
        return walletRepository.findByUserId(user.getId())
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
