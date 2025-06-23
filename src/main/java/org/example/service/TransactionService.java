package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransactionResponse;
import org.example.entity.Charge;
import org.example.entity.Transfer;
import org.example.entity.User;
import org.example.repository.ChargeRepository;
import org.example.repository.TransferRepository;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final ChargeRepository chargeRepository;
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions(User user, LocalDate from, LocalDate to) {
        // 사용자의 지갑 ID 조회
        Long walletId = user.getWallet() != null ?
                user.getWallet().getId() :
                walletRepository.findByUserIdFetchJoin(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("지갑이 없습니다."))
                        .getId();

        List<TransactionResponse> transactions = new ArrayList<>();

        // 1. 충전 내역 조회
        List<Charge> charges = (from != null && to != null)
                ? chargeRepository.findByWalletIdAndChargedAtBetweenWithFetch(
                walletId,
                from.atStartOfDay(),
                to.atTime(23, 59, 59))
                : chargeRepository.findByWalletIdWithFetch(walletId);

        charges.forEach(charge -> transactions.add(new TransactionResponse(
                charge.getId(),
                "CHARGE",
                (long) charge.getAmount(), // int를 Long으로 변환
                charge.getDescription(),
                charge.getChargedAt(),
                charge.getRealAccount().getBankName() + " " +
                        maskAccountNumber(charge.getRealAccount().getAccountNumber()),
                null // 잔액은 별도 계산 필요
        )));

        // 2. 보낸 송금 내역 조회
        List<Transfer> sentTransfers = (from != null && to != null)
                ? transferRepository.findBySenderIdAndTransferredAtBetweenFetchJoin(
                user.getId(),
                from.atStartOfDay(),
                to.atTime(23, 59, 59))
                : transferRepository.findBySenderIdFetchJoin(user.getId());

        sentTransfers.forEach(transfer -> transactions.add(new TransactionResponse(
                transfer.getId(),
                "TRANSFER_SENT",
                -transfer.getAmount(), // 마이너스로 표시
                transfer.getDescription(),
                transfer.getTransferredAt(),
                transfer.getReceiver().getName() + " (" +
                        maskEmail(transfer.getReceiver().getEmail()) + ")",
                null
        )));

        // 3. 받은 송금 내역 조회
        List<Transfer> receivedTransfers = (from != null && to != null)
                ? transferRepository.findByReceiverIdAndTransferredAtBetweenFetchJoin(
                user.getId(),
                from.atStartOfDay(),
                to.atTime(23, 59, 59))
                : transferRepository.findByReceiverIdFetchJoin(user.getId());

        receivedTransfers.forEach(transfer -> transactions.add(new TransactionResponse(
                transfer.getId(),
                "TRANSFER_RECEIVED",
                transfer.getAmount(), // 플러스로 표시
                transfer.getDescription(),
                transfer.getTransferredAt(),
                transfer.getSender().getName() + " (" +
                        maskEmail(transfer.getSender().getEmail()) + ")",
                null
        )));

        // 시간순 정렬 (최신순)
        transactions.sort(Comparator.comparing(TransactionResponse::getTransactionAt).reversed());

        return transactions;
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() <= 4) {
            return accountNumber;
        }
        return accountNumber.substring(0, 3) + "****" +
                accountNumber.substring(accountNumber.length() - 2);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email;
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}
