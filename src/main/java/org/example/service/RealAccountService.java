package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RealAccountRequest;
import org.example.dto.RealAccountResponse;
import org.example.entity.RealAccount;
import org.example.entity.User;
import org.example.repository.RealAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RealAccountService {
    private final RealAccountRepository realAccountRepository;

    public void registerAccount(User user, RealAccountRequest request) {
        RealAccount account = RealAccount.builder()
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .user(user)
                .build();

        realAccountRepository.save(account);
    }

    public List<RealAccountResponse> getMyAccounts(User user) {
        return realAccountRepository.findAllByUserId(user.getId()).stream()
                .map(a -> new RealAccountResponse(a.getId(), a.getBankName(), a.getAccountNumber(), a.getCreatedAt()))
                .toList();
    }

    public void deleteAccount(User user, Long id) {
        RealAccount account = realAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌가 존재하지 않습니다."));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        realAccountRepository.delete(account);
    }
}
