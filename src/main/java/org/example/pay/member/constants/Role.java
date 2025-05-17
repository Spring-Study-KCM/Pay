package org.example.pay.member.constants;

import lombok.Getter;

@Getter
public enum Role {
	USER("ROLE_USER");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}
}
