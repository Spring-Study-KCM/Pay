package org.example.domain.charge.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.charge.dto.ChargeRequest;
import org.example.domain.charge.dto.ChargeResponse;
import org.example.domain.user.entity.User;
import org.example.global.security.CustomUserPrincipal;
import org.example.domain.charge.service.ChargeService;
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
    public ResponseEntity<String> charge(@RequestBody ChargeRequest request, Authentication authentication) {
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
