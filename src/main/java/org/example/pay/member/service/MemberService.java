package org.example.pay.member.service;

import org.example.pay.member.domain.Member;
import org.example.pay.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Member getById(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("아이디에 맞는 멤버가 존재하지 않습니다.")
		);
	}

	public Member getByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(
			() -> new RuntimeException("이메일에 맞는 멤버가 존재하지 않습니다.")
		);
	}

	public Member getByMemberWithWallet(Long memberId) {
		return memberRepository.findByIdWithWallet(memberId).orElseThrow(
			() -> new RuntimeException("아이디에 맞는 멤버가 존재하지 않습니다.")
		);
	}

	public Member getByMemberWithWalletAndAccounts(Long memberId) {
		return memberRepository.findByIdWithWalletAndAccounts(memberId).orElseThrow(
			() -> new RuntimeException("아이디에 맞는 멤버가 존재하지 않습니다.")
		);
	}
}
