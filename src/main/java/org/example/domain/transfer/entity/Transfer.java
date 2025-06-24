package org.example.domain.transfer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.user.entity.User;
import org.example.global.constants.TransferStatus;

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
}
