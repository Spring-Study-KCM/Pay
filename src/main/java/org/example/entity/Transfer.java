package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private Long amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    private LocalDateTime transferredAt;

    @PrePersist
    public void prePersist() {
        this.transferredAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = TransferStatus.COMPLETED;
        }
    }

    public enum TransferStatus {
        COMPLETED,  // 송금 완료
        FAILED,     // 송금 실패
        CANCELLED   // 송금 취소
    }
}
