package org.example.pay.member.domain;

import java.util.ArrayList;
import java.util.List;

import org.example.pay.account.domain.Account;
import org.example.pay.common.entity.BaseEntity;
import org.example.pay.member.constants.Role;
import org.example.pay.wallet.domain.Wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NotNull
	private String email;

	private String name;

	@NotNull
	private String password;

	@Enumerated(EnumType.STRING)
	Role role;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Account> accounts = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;

	@Builder
	public Member(String email, String name, String password, Role role) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.role = role;
	}
}
