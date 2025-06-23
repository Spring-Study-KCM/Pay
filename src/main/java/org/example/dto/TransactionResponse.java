package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransactionResponse {  // 거래 조회용
    private Long id;
    private String type; // "CHARGE", "TRANSFER_SENT", "TRANSFER_RECEIVED"
    private Long amount;
    private String description;
    private LocalDateTime transactionAt;
    private String counterpartyInfo; // 충전: 은행명, 송금: 상대방 이름
    private Long balanceAfter; // 거래 후 잔액 (실제로는 계산 필요)
}