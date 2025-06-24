package org.example.domain.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.transfer.entity.Transfer;
import org.example.global.constants.TransferStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
    private Long id;
    private String receiverEmail;
    private String receiverName;
    private Long amount;
    private String description;
    private TransferStatus status;
    private LocalDateTime transferredAt;
}
