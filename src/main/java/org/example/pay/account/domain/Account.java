package org.example.pay.account.domain;

import org.example.pay.account.constants.BankCode;
import org.example.pay.common.entity.BaseEntity;
import org.example.pay.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private BankCode bankCode;

	private String accountNumber;

	private String name;

	private boolean isDefault;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public Account(BankCode bankCode, String accountNumber, String name, boolean isDefault) {
		this.bankCode = bankCode;
		this.accountNumber = accountNumber;
		this.name = name;
		this.isDefault = isDefault;
	}

	public void setMember(Member member) {
		this.member = member;
		member.getAccounts().add(this);
	}
}
