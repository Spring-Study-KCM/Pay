package org.example.pay.account.pay_account.domain;

import org.example.pay.account.pay_account.dto.response.PayType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "TRANSACTIONS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transactions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transactions_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_account_id")
	private PayAccount payAccount;

	@Column(name = "amount")
	private Long amount;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "pay_type")
	private PayType payType;

	@Column(name = "is_success")
	private boolean isSuccess;

	@Builder
	public Transactions(Long amount, boolean isSuccess, PayAccount payAccount, PayType payType) {
		this.amount = amount;
		this.isSuccess = isSuccess;
		this.payAccount = payAccount;
		this.payType = payType;
	}

	public void updateSuccess() {
		this.isSuccess = true;
	}
}
