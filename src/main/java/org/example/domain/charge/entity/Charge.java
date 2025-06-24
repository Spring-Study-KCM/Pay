package org.example.domain.charge.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.domain.wallet.entity.Wallet;
import org.example.domain.account.entity.RealAccount;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    private String description;

    private LocalDateTime chargedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_account_id")
    private RealAccount realAccount;

    @PrePersist
    public void prePersist() {
        this.chargedAt = LocalDateTime.now();
    }
}
