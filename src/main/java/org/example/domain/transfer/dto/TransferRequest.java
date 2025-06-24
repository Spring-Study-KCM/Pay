package org.example.domain.transfer.dto;

import lombok.Getter;

@Getter
public class TransferRequest {
    private String receiverEmail;
    private Long amount;
    private String description;
}
