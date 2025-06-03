package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChargeRequest;
import org.example.dto.ChargeResponse;
import org.example.entity.Charge;
import org.example.entity.RealAccount;
import org.example.entity.User;
import org.example.entity.Wallet;
import org.example.repository.ChargeRepository;
import org.example.repository.RealAccountRepository;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final RealAccountRepository realAccountRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void chargeWallet(User user, ChargeRequest request) {
        Wallet wallet = walletRepository.findByUserIdFetchJoin(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("지갑이 없습니다."));

        RealAccount account = realAccountRepository.findById(request.getRealAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계좌가 없습니다."));

        wallet.setBalance(wallet.getBalance() + request.getAmount());

        Charge charge = Charge.builder()
                .wallet(wallet)
                .realAccount(account)
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        chargeRepository.save(charge);
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> getChargeHistory(User user, LocalDate from, LocalDate to) {
        Wallet wallet = walletRepository.findByUserIdFetchJoin(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("지갑이 없습니다."));

        List<Charge> charges = (from != null && to != null)
                ? chargeRepository.findByWalletIdAndChargedAtBetweenWithFetch(
                wallet.getId(),
                from.atStartOfDay(),
                to.atTime(23, 59, 59))
                : chargeRepository.findByWalletIdWithFetch(wallet.getId());

        return charges.stream()
                .map(c -> new ChargeResponse(
                        c.getId(),
                        c.getAmount(),
                        c.getDescription(),
                        c.getChargedAt(),
                        c.getRealAccount().getBankName(),
                        c.getRealAccount().getAccountNumber()
                ))
                .toList();
    }
}

