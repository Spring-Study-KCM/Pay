package org.example.pay.auth.service;

import org.example.pay.auth.dto.request.LoginRequestDto;
import org.example.pay.member.constants.Role;
import org.example.pay.member.domain.Member;
import org.example.pay.auth.dto.request.JoinRequestDto;
import org.example.pay.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public Member join(JoinRequestDto joinRequestDto) {
		if (memberRepository.existsByEmail(joinRequestDto.email())) {
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		}

		Member member = Member.builder()
			.email(joinRequestDto.email())
			.password(bCryptPasswordEncoder.encode(joinRequestDto.password()))
			.name(joinRequestDto.name())
			.role(Role.USER)
			.build();
		memberRepository.save(member);

		return member;
	}
}
