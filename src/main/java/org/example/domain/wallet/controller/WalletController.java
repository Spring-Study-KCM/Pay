package org.example.domain.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.user.entity.User;
import org.example.domain.wallet.entity.Wallet;
import org.example.global.security.CustomUserPrincipal;
import org.example.domain.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> getMyWallet(Authentication authentication) {
        // Authentication에서 User 직접 추출
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        Wallet wallet = walletService.getWalletByUser(user);
        return ResponseEntity.ok(new WalletResponse(wallet.getBalance(), wallet.getCreatedAt()));
    }

    private record WalletResponse(Long balance, java.time.LocalDateTime createdAt) {}
}
