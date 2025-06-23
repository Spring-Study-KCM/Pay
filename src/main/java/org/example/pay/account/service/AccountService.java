package org.example.pay.account.service;

import java.util.List;

import org.example.pay.account.bank_account.domain.BankAccount;
import org.example.pay.account.bank_account.dto.response.BankAccountInfoResponse;
import org.example.pay.account.bank_account.service.BankAccountService;
import org.example.pay.account.pay_account.domain.PayAccount;
import org.example.pay.account.pay_account.domain.Transactions;
import org.example.pay.account.pay_account.dto.response.PayType;
import org.example.pay.account.pay_account.repository.TransactionsRepository;
import org.example.pay.account.pay_account.service.PayAccountService;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.FriendErrorCode;
import org.example.pay.global.exception.code.PayAccountErrorCode;
import org.example.pay.global.exception.code.TransactionsErrorCode;
import org.example.pay.user.domain.User;
import org.example.pay.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

	private final PayAccountService payAccountService;
	private final BankAccountService bankAccountService;
	private final UserRepository userRepository;
	private final TransactionsRepository transactionsRepository;

	@Transactional
	public void reloadAccount(Long bankId, Long depositAmount) {
		BankAccount bankAccount = bankAccountService.getBankAccountWithPayAccount(bankId);
		PayAccount payAccount = bankAccount.getPayAccount();

		bankAccountService.withdraw(bankAccount, depositAmount);
		payAccountService.deposit(payAccount, depositAmount);
	}

	@Transactional
	public void connectBankAccount(Long userId, String bankName, String bankAccountNumber) {
		bankAccountService.connectBankAccount(userId, bankName, bankAccountNumber);
	}

	@Transactional
	public void disconnectBankAccount(Long bankId) {
		bankAccountService.disConnectBankAccount(bankId);
	}

	public List<BankAccountInfoResponse> getConnectedBankAccount(Long userId) {
		return bankAccountService.getConnectedBankAccount(userId);
	}

	@Transactional
	public void createPayAccount(User user) {
		payAccountService.createPayAccount(user);
	}

	@Transactional
	public void remit(Long friendId, Long amount, Long userId) {

		boolean isFriend = userRepository.isFriend(userId, friendId);

		if (!isFriend) {
			throw new PayException(FriendErrorCode.FRIEND_NOT_FOUND);
		}

		PayAccount myPayAccount = payAccountService.getPayAccount(userId);

		if (myPayAccount.getBalance() < amount) {
			throw new PayException(PayAccountErrorCode.NOT_ENOUGH_PAY_BALANCE);
		}

		myPayAccount.withdraw(amount);

		Transactions myTransactions = Transactions.builder()
			.payAccount(myPayAccount)
			.payType(PayType.WITHDRAW)
			.amount(amount)
			.isSuccess(false)
			.build();

		transactionsRepository.save(myTransactions);
	}

    @Transactional
	public void deposit(Long transactionsId, Long friendId, Long amount, Long userId) {

		boolean isFriend = userRepository.isFriend(userId, friendId);

		if (!isFriend) {
			throw new PayException(FriendErrorCode.FRIEND_NOT_FOUND);
		}

		PayAccount myPayAccount = payAccountService.getPayAccount(userId);

		myPayAccount.deposit(amount);

        Transactions friendTransactions = transactionsRepository.findById(transactionsId)
            .orElseThrow(() -> new PayException(TransactionsErrorCode.NOT_FOUND_TRANSACTIONS));

        friendTransactions.updateSuccess();

        Transactions myTransactions = Transactions.builder()
			.payAccount(myPayAccount)
			.payType(PayType.DEPOSIT)
			.amount(amount)
			.isSuccess(true)
			.build();

		transactionsRepository.save(myTransactions);
	}
}