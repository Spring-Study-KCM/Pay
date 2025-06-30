package org.example.domain.charge.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.charge.dto.ChargeRequest;
import org.example.domain.charge.dto.ChargeResponse;
import org.example.domain.charge.entity.Charge;
import org.example.domain.account.entity.RealAccount;
import org.example.domain.user.entity.User;
import org.example.domain.wallet.entity.Wallet;
import org.example.domain.charge.repository.ChargeRepository;
import org.example.domain.account.repository.RealAccountRepository;
import org.example.domain.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final RealAccountRepository realAccountRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void chargeWallet(User user, ChargeRequest request) {
        log.info("충전 시작 - 사용자: {}, 금액: {}", user.getEmail(), request.getAmount());

        validateChargeRequest(request);
        final Wallet wallet = getWalletForUser(user);
        final RealAccount account = getValidatedAccount(request.getRealAccountId(), user);

        executeCharge(wallet, (long) request.getAmount());
        saveChargeRecord(wallet, account, request);
    }

    public List<ChargeResponse> getChargeHistory(User user, LocalDate from, LocalDate to) {
        final Wallet wallet = getWalletForUser(user);
        final List<Charge> charges = getChargesInDateRange(wallet.getId(), from, to);

        return charges.stream()
                .map(this::buildChargeResponse)
                .toList();
    }

    private void validateChargeRequest(ChargeRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    private Wallet getWalletForUser(User user) {
        if (user.getWallet() != null) {
            return user.getWallet();
        }

        log.info("User에서 Wallet을 찾을 수 없어 DB에서 조회합니다.");
        return walletRepository.findByUserIdFetchJoin(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("지갑이 없습니다."));
    }

    private RealAccount getValidatedAccount(Long accountId, User user) {
        final RealAccount account = realAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계좌가 없습니다."));

        validateAccountOwnership(account, user);
        return account;
    }

    private void validateAccountOwnership(RealAccount account, User user) {
        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("계좌 소유자가 일치하지 않습니다.");
        }
    }

    private void executeCharge(Wallet wallet, Long amount) {
        final Long oldBalance = wallet.getBalance();
        final Long newBalance = oldBalance + amount;

        log.info("충전 전 잔액: {}", oldBalance);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        log.info("잔액 업데이트 및 저장 완료: {} -> {}", oldBalance, newBalance);
    }

    private void saveChargeRecord(Wallet wallet, RealAccount account, ChargeRequest request) {
        final Charge charge = Charge.builder()
                .wallet(wallet)
                .realAccount(account)
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        final Charge savedCharge = chargeRepository.save(charge);
        log.info("충전 완료 - 충전 ID: {}", savedCharge.getId());
    }

    private List<Charge> getChargesInDateRange(Long walletId, LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return chargeRepository.findByWalletIdAndChargedAtBetweenWithFetch(
                    walletId, from.atStartOfDay(), to.atTime(23, 59, 59));
        }
        return chargeRepository.findByWalletIdWithFetch(walletId);
    }

    private ChargeResponse buildChargeResponse(Charge charge) {
        return new ChargeResponse(
                charge.getId(),
                charge.getAmount(),
                charge.getDescription(),
                charge.getChargedAt(),
                charge.getRealAccount().getBankName(),
                charge.getRealAccount().getAccountNumber()
        );
    }
}

