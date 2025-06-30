package org.example.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.transaction.dto.PaginatedTransactionResponse;
import org.example.domain.transaction.dto.TransactionRequest;
import org.example.domain.transaction.dto.TransactionResponse;
import org.example.domain.charge.entity.Charge;
import org.example.domain.transfer.entity.Transfer;
import org.example.domain.user.entity.User;
import org.example.domain.charge.repository.ChargeRepository;
import org.example.domain.transfer.repository.TransferRepository;
import org.example.domain.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final ChargeRepository chargeRepository;
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public PaginatedTransactionResponse getAllTransactions(User user, TransactionRequest request) {
        final Long walletId = getWalletId(user);
        final LocalDateTime fromDateTime = request.getFrom().atStartOfDay();
        final LocalDateTime toDateTime = request.getTo().atTime(23, 59, 59);

        final List<TransactionResponse> allTransactions = buildAllTransactions(user, walletId, fromDateTime, toDateTime);
        final List<TransactionResponse> sortedTransactions = sortTransactionsByTime(allTransactions);

        return paginateTransactions(sortedTransactions, request.getPage(), request.getSize());
    }

    private Long getWalletId(User user) {
        if (user.getWallet() != null) {
            return user.getWallet().getId();
        }

        return walletRepository.findByUserIdFetchJoin(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("지갑이 없습니다."))
                .getId();
    }

    private List<TransactionResponse> buildAllTransactions(User user, Long walletId, LocalDateTime from, LocalDateTime to) {
        final List<TransactionResponse> transactions = new ArrayList<>();

        transactions.addAll(buildChargeTransactions(walletId, from, to));
        transactions.addAll(buildTransferTransactions(user.getId(), from, to));

        return transactions;
    }

    private List<TransactionResponse> buildChargeTransactions(Long walletId, LocalDateTime from, LocalDateTime to) {
        final List<Charge> charges = chargeRepository.findByWalletIdAndChargedAtBetweenWithFetch(walletId, from, to);

        return charges.stream()
                .map(this::buildChargeTransaction)
                .toList();
    }

    private TransactionResponse buildChargeTransaction(Charge charge) {
        final String counterpartyInfo = buildBankInfo(charge.getRealAccount().getBankName(),
                charge.getRealAccount().getAccountNumber());

        return TransactionResponse.builder()
                .id(charge.getId())
                .type("CHARGE")
                .amount((long) charge.getAmount())
                .description(charge.getDescription())
                .transactionAt(charge.getChargedAt())
                .counterpartyInfo(counterpartyInfo)
                .balanceAfter(null)
                .build();
    }

    private List<TransactionResponse> buildTransferTransactions(Long userId, LocalDateTime from, LocalDateTime to) {
        final List<Transfer> transfers = transferRepository.findAllByUserIdAndTransferredAtBetweenFetchJoin(userId, from, to);

        return transfers.stream()
                .map(transfer -> buildTransferTransaction(transfer, userId))
                .toList();
    }

    private TransactionResponse buildTransferTransaction(Transfer transfer, Long userId) {
        final boolean isSender = transfer.getSender().getId().equals(userId);

        if (isSender) {
            return buildSentTransferTransaction(transfer);
        }
        return buildReceivedTransferTransaction(transfer);
    }

    private TransactionResponse buildSentTransferTransaction(Transfer transfer) {
        final String counterpartyInfo = buildUserInfo(transfer.getReceiver().getName(),
                transfer.getReceiver().getEmail());

        return TransactionResponse.builder()
                .id(transfer.getId())
                .type("TRANSFER_SENT")
                .amount(-transfer.getAmount())
                .description(transfer.getDescription())
                .transactionAt(transfer.getTransferredAt())
                .counterpartyInfo(counterpartyInfo)
                .balanceAfter(null)
                .build();
    }

    private TransactionResponse buildReceivedTransferTransaction(Transfer transfer) {
        final String counterpartyInfo = buildUserInfo(transfer.getSender().getName(),
                transfer.getSender().getEmail());

        return TransactionResponse.builder()
                .id(transfer.getId())
                .type("TRANSFER_RECEIVED")
                .amount(transfer.getAmount())
                .description(transfer.getDescription())
                .transactionAt(transfer.getTransferredAt())
                .counterpartyInfo(counterpartyInfo)
                .balanceAfter(null)
                .build();
    }

    private String buildBankInfo(String bankName, String accountNumber) {
        return bankName + " " + maskAccountNumber(accountNumber);
    }

    private String buildUserInfo(String name, String email) {
        return name + " (" + maskEmail(email) + ")";
    }

    private List<TransactionResponse> sortTransactionsByTime(List<TransactionResponse> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing(TransactionResponse::getTransactionAt).reversed())
                .toList();
    }

    private PaginatedTransactionResponse paginateTransactions(List<TransactionResponse> transactions, int page, int size) {
        final int totalElements = transactions.size();
        final int totalPages = (int) Math.ceil((double) totalElements / size);
        final int startIndex = page * size;
        final int endIndex = Math.min(startIndex + size, totalElements);

        final List<TransactionResponse> pagedTransactions = getPagedTransactions(transactions, startIndex, endIndex);

        return PaginatedTransactionResponse.builder()
                .transactions(pagedTransactions)
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNext(page < totalPages - 1)
                .hasPrevious(page > 0)
                .build();
    }

    private List<TransactionResponse> getPagedTransactions(List<TransactionResponse> transactions, int startIndex, int endIndex) {
        if (startIndex >= transactions.size()) {
            return new ArrayList<>();
        }
        return transactions.subList(startIndex, endIndex);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() <= 4) {
            return accountNumber;
        }
        return accountNumber.substring(0, 3) + "****" + accountNumber.substring(accountNumber.length() - 2);
    }

    private String maskEmail(String email) {
        final int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email;
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}
