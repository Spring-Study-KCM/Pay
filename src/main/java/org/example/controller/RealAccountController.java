package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.RealAccountRequest;
import org.example.dto.RealAccountResponse;
import org.example.entity.User;
import org.example.security.CustomUserPrincipal;
import org.example.service.RealAccountService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/real-accounts")
@RequiredArgsConstructor
public class RealAccountController {

    private final RealAccountService realAccountService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RealAccountRequest request, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        realAccountService.registerAccount(user, request);
        return ResponseEntity.ok("계좌 등록 완료");
    }

    @GetMapping
    public ResponseEntity<List<RealAccountResponse>> getMyAccounts(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        return ResponseEntity.ok(realAccountService.getMyAccounts(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        realAccountService.deleteAccount(user, id);
        return ResponseEntity.ok("계좌 삭제 완료");
    }
}
