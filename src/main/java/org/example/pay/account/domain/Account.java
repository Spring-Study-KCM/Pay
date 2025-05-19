package org.example.pay.account.domain;

import org.example.pay.common.entity.BaseEntity;
import org.example.pay.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int bankCode;

	private String accountNumber;

	private String name;

	private boolean isDefault;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
