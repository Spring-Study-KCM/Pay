package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChargeResponse {
    private Long id;
    private int amount;
    private String description;
    private LocalDateTime chargedAt;
    private String bankName;
    private String accountNumber;
}
