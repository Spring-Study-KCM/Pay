package org.example.pay.wallet.domain;

import java.util.UUID;

import org.example.pay.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Wallet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String uuid;

	private Long balance = 0L;

	public Wallet() {
		this.uuid = UUID.randomUUID().toString();
	}

	public void charge(Long amount) {
		this.balance += amount;
	}
}
