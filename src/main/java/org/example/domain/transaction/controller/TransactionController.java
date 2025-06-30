package org.example.domain.transaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.transaction.dto.PaginatedTransactionResponse;
import org.example.domain.transaction.dto.TransactionRequest;
import org.example.domain.transaction.dto.TransactionResponse;
import org.example.domain.user.entity.User;
import org.example.global.security.CustomUserPrincipal;
import org.example.domain.transaction.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<PaginatedTransactionResponse> getAllTransactions(
            Authentication authentication,
            @Valid @ModelAttribute TransactionRequest request
    ) {
        final User user = extractUser(authentication);
        final PaginatedTransactionResponse response = transactionService.getAllTransactions(user, request);

        return ResponseEntity.ok(response);
    }

    private User extractUser(Authentication authentication) {
        final CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        return principal.getUser();
    }
}
