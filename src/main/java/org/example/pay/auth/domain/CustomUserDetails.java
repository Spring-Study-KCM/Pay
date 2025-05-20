package org.example.pay.auth.domain;

import java.util.Collection;
import java.util.List;

import org.example.pay.member.constants.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	private final Long id;
	private final String email;
	private final String password;
	private final Role role;

	public CustomUserDetails(Long id, String email, String password, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(role::name);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public long getId() {
		return id;
	}
}
