package org.example.pay.auth.service;

import java.util.Collections;

import org.example.pay.member.domain.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SecurityContextService {
	public void saveSecurityContext(HttpSession session, Member member) {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());

		Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, Collections.singleton(authority));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
	}
}
