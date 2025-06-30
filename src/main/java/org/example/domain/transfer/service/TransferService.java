package org.example.domain.transfer.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.transfer.dto.TransferRequest;
import org.example.domain.transfer.dto.TransferResponse;
import org.example.domain.transfer.entity.Transfer;
import org.example.domain.user.entity.User;
import org.example.domain.wallet.entity.Wallet;
import org.example.domain.transfer.repository.TransferRepository;
import org.example.domain.user.repository.UserRepository;
import org.example.domain.wallet.repository.WalletRepository;
import org.example.global.constants.TransferStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void transfer(User sender, TransferRequest request) {
        log.info("송금 시작 - 송금자: {}, 받는사람: {}, 금액: {}",
                sender.getEmail(), request.getReceiverEmail(), request.getAmount());

        validateTransferRequest(request);
        final User receiver = findReceiver(request.getReceiverEmail());
        validateTransferUsers(sender, receiver);

        final TransferContext context = createTransferContext(sender, receiver, request);
        executeTransfer(context);
        saveTransferRecord(context);

        log.info("송금 완료 - 송금 ID: {}", context.getTransferId());
    }

    public List<TransferResponse> getTransferHistory(User user, LocalDate from, LocalDate to) {
        final List<Transfer> transfers = getTransfersInDateRange(user.getId(), from, to);

        return transfers.stream()
                .map(this::buildTransferResponse)
                .toList();
    }

    private void validateTransferRequest(TransferRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("송금 금액은 0보다 커야 합니다.");
        }
    }

    private User findReceiver(String receiverEmail) {
        return userRepository.findByEmailFetchJoin(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("받는 사람을 찾을 수 없습니다."));
    }

    private void validateTransferUsers(User sender, User receiver) {
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("자기 자신에게는 송금할 수 없습니다.");
        }
    }

    private TransferContext createTransferContext(User sender, User receiver, TransferRequest request) {
        final Wallet senderWallet = getWalletWithValidation(sender, "송금자");
        final Wallet receiverWallet = getWalletWithValidation(receiver, "받는 사람");

        validateSufficientBalance(senderWallet, request.getAmount());

        return TransferContext.builder()
                .sender(sender)
                .receiver(receiver)
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }

    private Wallet getWalletWithValidation(User user, String userType) {
        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = walletRepository.findByUserIdFetchJoin(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException(userType + "의 지갑이 없습니다."));
        }
        log.info("{} 현재 잔액: {}", userType, wallet.getBalance());
        return wallet;
    }

    private void validateSufficientBalance(Wallet senderWallet, Long amount) {
        if (senderWallet.getBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }

    private void executeTransfer(TransferContext context) {
        final Long senderOldBalance = context.getSenderWallet().getBalance();
        final Long receiverOldBalance = context.getReceiverWallet().getBalance();

        updateWalletBalances(context);
        saveWallets(context);

        logBalanceChanges(senderOldBalance, receiverOldBalance, context);
    }

    private void updateWalletBalances(TransferContext context) {
        final Wallet senderWallet = context.getSenderWallet();
        final Wallet receiverWallet = context.getReceiverWallet();
        final Long amount = context.getAmount();

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);
    }

    private void saveWallets(TransferContext context) {
        walletRepository.save(context.getSenderWallet());
        walletRepository.save(context.getReceiverWallet());
    }

    private void logBalanceChanges(Long senderOldBalance, Long receiverOldBalance, TransferContext context) {
        log.info("송금자 잔액 변경: {} -> {}", senderOldBalance, context.getSenderWallet().getBalance());
        log.info("수신자 잔액 변경: {} -> {}", receiverOldBalance, context.getReceiverWallet().getBalance());
    }

    private void saveTransferRecord(TransferContext context) {
        final Transfer transfer = Transfer.builder()
                .sender(context.getSender())
                .receiver(context.getReceiver())
                .amount(context.getAmount())
                .description(context.getDescription())
                .status(TransferStatus.COMPLETED)
                .build();

        final Transfer savedTransfer = transferRepository.save(transfer);
        context.setTransferId(savedTransfer.getId());
    }

    private List<Transfer> getTransfersInDateRange(Long userId, LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return transferRepository.findBySenderIdAndTransferredAtBetweenFetchJoin(
                    userId, from.atStartOfDay(), to.atTime(23, 59, 59));
        }
        return transferRepository.findBySenderIdFetchJoin(userId);
    }

    private TransferResponse buildTransferResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .receiverEmail(transfer.getReceiver().getEmail())
                .receiverName(transfer.getReceiver().getName())
                .amount(transfer.getAmount())
                .description(transfer.getDescription())
                .status(transfer.getStatus())
                .transferredAt(transfer.getTransferredAt())
                .build();
    }

    // 내부 클래스: 송금 컨텍스트
    @Getter
    @Builder
    private static class TransferContext {
        private final User sender;
        private final User receiver;
        private final Wallet senderWallet;
        private final Wallet receiverWallet;
        private final Long amount;
        private final String description;

        @Setter
        private Long transferId;
    }
}
