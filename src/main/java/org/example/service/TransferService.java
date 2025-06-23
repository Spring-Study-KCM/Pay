package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransferRequest;
import org.example.dto.TransferResponse;
import org.example.entity.Transfer;
import org.example.entity.User;
import org.example.entity.Wallet;
import org.example.repository.TransferRepository;
import org.example.repository.UserRepository;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void transfer(User sender, TransferRequest request) {
        // 송금할 금액 검증
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("송금 금액은 0보다 커야 합니다.");
        }

        // 받는 사람 조회
        User receiver = userRepository.findByEmailFetchJoin(request.getReceiverEmail())
                .orElseThrow(() -> new IllegalArgumentException("받는 사람을 찾을 수 없습니다."));

        // 자기 자신에게 송금 방지
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("자기 자신에게는 송금할 수 없습니다.");
        }

        // 보내는 사람의 지갑 조회 및 잔액 확인
        Wallet senderWallet = sender.getWallet();
        if (senderWallet == null) {
            senderWallet = walletRepository.findByUserIdFetchJoin(sender.getId())
                    .orElseThrow(() -> new IllegalArgumentException("송금자의 지갑이 없습니다."));
        }

        if (senderWallet.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        // 받는 사람의 지갑 조회
        Wallet receiverWallet = receiver.getWallet();
        if (receiverWallet == null) {
            receiverWallet = walletRepository.findByUserIdFetchJoin(receiver.getId())
                    .orElseThrow(() -> new IllegalArgumentException("받는 사람의 지갑이 없습니다."));
        }

        // 송금 처리
        senderWallet.setBalance(senderWallet.getBalance() - request.getAmount());
        receiverWallet.setBalance(receiverWallet.getBalance() + request.getAmount());

        // 송금 기록 저장
        Transfer transfer = Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(Transfer.TransferStatus.COMPLETED)
                .build();

        transferRepository.save(transfer);
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> getTransferHistory(User user, LocalDate from, LocalDate to) {
        List<Transfer> transfers = (from != null && to != null)
                ? transferRepository.findBySenderIdAndTransferredAtBetweenFetchJoin(
                user.getId(),
                from.atStartOfDay(),
                to.atTime(23, 59, 59))
                : transferRepository.findBySenderIdFetchJoin(user.getId());

        return transfers.stream()
                .map(t -> new TransferResponse(
                        t.getId(),
                        t.getReceiver().getEmail(),
                        t.getReceiver().getName(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getStatus(),
                        t.getTransferredAt()
                ))
                .toList();
    }
}
