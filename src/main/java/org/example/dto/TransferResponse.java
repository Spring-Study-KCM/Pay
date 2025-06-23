package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Transfer;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransferResponse {
    private Long id;
    private String receiverEmail;
    private String receiverName;
    private Long amount;
    private String description;
    private Transfer.TransferStatus status;
    private LocalDateTime transferredAt;
}
