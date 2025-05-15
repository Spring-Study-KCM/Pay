package org.example.pay.member.service;

import java.util.Optional;

import org.example.pay.member.constants.Role;
import org.example.pay.member.domain.Member;
import org.example.pay.member.dto.JoinDto;
import org.example.pay.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public Member join(JoinDto joinDto) {
		if (memberRepository.existsByEmail(joinDto.email())) {
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		}

		Member member = Member.builder()
			.email(joinDto.email())
			.password(bCryptPasswordEncoder.encode(joinDto.password()))
			.role(Role.USER)
			.build();
		memberRepository.save(member);

		return member;
	}

	public Optional<Member> getByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
}
