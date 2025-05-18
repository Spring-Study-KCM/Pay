package org.example.pay.pay_account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.pay.user.domain.User;

@Getter
@Table(name = "PAY_ACCOUNT")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_account_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "balance")
    private Long balance = 0L;

    @Builder
    public PayAccount(User user) {
        this.user = user;
    }
}
