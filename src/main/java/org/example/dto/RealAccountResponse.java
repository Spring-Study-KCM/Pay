package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RealAccountResponse {
    private Long id;
    private String bankName;
    private String accountNumber;
    private LocalDateTime createdAt;
}
