package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.entity.Wallet;
import org.example.security.CustomUserPrincipal;
import org.example.service.UserService;
import org.example.service.WalletService;
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
    public ResponseEntity<?> getMyWallet(Authentication authentication) {
        // Authentication에서 User 직접 추출
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        Wallet wallet = walletService.getWalletByUser(user);
        return ResponseEntity.ok(new WalletResponse(wallet.getBalance(), wallet.getCreatedAt()));
    }

    private record WalletResponse(Long balance, java.time.LocalDateTime createdAt) {}
}
