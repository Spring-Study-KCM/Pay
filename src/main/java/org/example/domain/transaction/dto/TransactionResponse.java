package org.example.domain.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {  // 거래 조회용
    private Long id;
    private String type; // "CHARGE", "TRANSFER_SENT", "TRANSFER_RECEIVED"
    private Long amount;
    private String description;
    private LocalDateTime transactionAt;
    private String counterpartyInfo; // 충전: 은행명, 송금: 상대방 이름
    private Long balanceAfter; // 거래 후 잔액
}