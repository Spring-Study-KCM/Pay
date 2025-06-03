package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChargeRequest;
import org.example.dto.ChargeResponse;
import org.example.entity.User;
import org.example.security.CustomUserPrincipal;
import org.example.service.ChargeService;
import org.example.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/charges")
@RequiredArgsConstructor
public class ChargeController {
    private final ChargeService chargeService;

    @PostMapping
    public ResponseEntity<?> charge(@RequestBody ChargeRequest request, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        chargeService.chargeWallet(user, request);
        return ResponseEntity.ok("충전 완료");
    }

    @GetMapping
    public ResponseEntity<List<ChargeResponse>> getHistory(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
    ) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        return ResponseEntity.ok(chargeService.getChargeHistory(user, from, to));
    }
}
