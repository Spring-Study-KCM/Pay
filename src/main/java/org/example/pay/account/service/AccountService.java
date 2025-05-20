package org.example.pay.account.service;

import java.util.List;

import org.example.pay.account.domain.Account;
import org.example.pay.account.dto.request.AddAccountRequestDto;
import org.example.pay.account.dto.response.AccountResponseDto;
import org.example.pay.account.repository.AccountRepository;
import org.example.pay.member.domain.Member;
import org.example.pay.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final MemberService memberService;
	private final AccountRepository accountRepository;

	public List<AccountResponseDto> getAccounts(Long memberId) {
		Member member = memberService.getById(memberId);
		List<Account> accounts = member.getAccounts();

		return accounts.stream().map(AccountResponseDto::of).toList();
	}

	@Transactional
	public void addAccount(AddAccountRequestDto accountRequestDto, Long memberId) {
		Member member = memberService.getById(memberId);

		if(member.getAccounts().size() >= 3) {
			throw new IllegalArgumentException("계좌는 3개까지 등록 가능합니다.");
		}

		Account account = Account.builder()
			.bankCode(accountRequestDto.bankCode())
			.accountNumber(accountRequestDto.accountNumber())
			.name(accountRequestDto.name())
			.isDefault(accountRequestDto.isDefault())
			.build();
		account.setMember(member);

		accountRepository.save(account);
	}

	@Transactional
	public void deleteAccount(Long memberId, String uuid) {
		Member member = memberService.getById(memberId);

		Account account = getByUuid(uuid);
		if(!member.getAccounts().contains(account)) {
			throw new IllegalArgumentException("등록된 계좌가 아닙니다");
		}

		accountRepository.delete(account);
	}

	public Account getByUuid(String uuid) {
		return accountRepository.findByUuid(uuid).orElseThrow(
			() -> new RuntimeException("계좌 정보가 없습니다.")
		);
	}

	public Account getById(Long accountId) {
		return accountRepository.findById(accountId).orElseThrow(
			() -> new RuntimeException("계좌 정보가 없습니다.")
		);
	}
}
