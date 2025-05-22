package org.example.pay.wallet.service;

import org.example.pay.account.domain.Account;
import org.example.pay.account.service.AccountService;
import org.example.pay.member.domain.Member;
import org.example.pay.member.service.MemberService;
import org.example.pay.wallet.domain.Wallet;
import org.example.pay.wallet.dto.request.ChargeRequestDto;
import org.example.pay.wallet.dto.response.WalletResponseDto;
import org.example.pay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {

	private final WalletRepository walletRepository;
	private final MemberService memberService;
	private final AccountService accountService;

	@Transactional
	public void createWallet(Long memberId) {
		Member member = memberService.getById(memberId);
		if(member.getWallet() != null) {
			throw new IllegalArgumentException("이미 지갑이 있습니다.");
		}
		Wallet wallet = new Wallet();
		member.setWallet(wallet);
		walletRepository.save(wallet);
	}

	public WalletResponseDto getWallet(Long memberId) {
		Member member = memberService.getByMemberWithWallet(memberId);
		Wallet wallet = member.getWallet();
		return WalletResponseDto.of(wallet);
	}

	@Transactional
	public void chargePayMoney(ChargeRequestDto requestDto, Long memberId) {
		Member member = memberService.getByMemberWithWalletAndAccounts(memberId);
		Wallet wallet = member.getWallet();
		Account account = accountService.getById(requestDto.accountId());

		if(!member.getAccounts().contains(account)) {
			throw new IllegalArgumentException("등록된 계좌가 아닙니다");
		}

		wallet.charge(requestDto.amount());
	}
}
