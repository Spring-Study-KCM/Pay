package org.example.domain.transfer.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.transfer.dto.TransferRequest;
import org.example.domain.transfer.dto.TransferResponse;
import org.example.domain.user.entity.User;
import org.example.global.security.CustomUserPrincipal;
import org.example.domain.transfer.service.TransferService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        transferService.transfer(user, request);
        return ResponseEntity.ok("송금이 완료되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getTransferHistory(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to
    ) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        return ResponseEntity.ok(transferService.getTransferHistory(user, from, to));
    }
}
