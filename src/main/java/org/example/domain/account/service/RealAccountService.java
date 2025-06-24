package org.example.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.account.dto.RealAccountRequest;
import org.example.domain.account.dto.RealAccountResponse;
import org.example.domain.account.entity.RealAccount;
import org.example.domain.user.entity.User;
import org.example.domain.account.repository.RealAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RealAccountService {
    private final RealAccountRepository realAccountRepository;

    @Transactional
    public void registerAccount(User user, RealAccountRequest request) {
        // 동일한 계좌번호 중복 등록 방지
        List<RealAccount> existingAccounts = realAccountRepository.findAllByUserIdFetchJoin(user.getId());
        boolean isDuplicate = existingAccounts.stream()
                .anyMatch(account -> account.getAccountNumber().equals(request.getAccountNumber())
                        && account.getBankName().equals(request.getBankName()));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 등록된 계좌입니다.");
        }

        RealAccount account = RealAccount.builder()
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .user(user)
                .build();

        realAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<RealAccountResponse> getMyAccounts(User user) {
        return realAccountRepository.findAllByUserIdFetchJoin(user.getId()).stream()
                .map(a -> new RealAccountResponse(a.getId(), a.getBankName(), a.getAccountNumber(), a.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void deleteAccount(User user, Long id) {
        RealAccount account = realAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌가 존재하지 않습니다."));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        realAccountRepository.delete(account);
    }

    @Transactional(readOnly = true)
    public RealAccount getAccountWithPermissionCheck(User user, Long accountId) {
        RealAccount account = realAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌가 존재하지 않습니다."));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("계좌 접근 권한이 없습니다.");
        }

        return account;
    }


//    private final RealAccountRepository realAccountRepository;
//
//    public void registerAccount(User user, RealAccountRequest request) {
//        RealAccount account = RealAccount.builder()
//                .bankName(request.getBankName())
//                .accountNumber(request.getAccountNumber())
//                .user(user)
//                .build();
//
//        realAccountRepository.save(account);
//    }
//
//    public List<RealAccountResponse> getMyAccounts(User user) {
//        return realAccountRepository.findAllByUserIdFetchJoin(user.getId()).stream()
//                .map(a -> new RealAccountResponse(a.getId(), a.getBankName(), a.getAccountNumber(), a.getCreatedAt()))
//                .toList();
//    }
//
//    public void deleteAccount(User user, Long id) {
//        RealAccount account = realAccountRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 계좌가 존재하지 않습니다."));
//
//        if (!account.getUser().getId().equals(user.getId())) {
//            throw new IllegalArgumentException("삭제 권한이 없습니다.");
//        }
//
//        realAccountRepository.delete(account);
//    }
//
//    // 특정 계좌 조회 시 권한 검증 포함
//    public RealAccount getAccountWithPermissionCheck(User user, Long accountId) {
//        RealAccount account = realAccountRepository.findById(accountId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 계좌가 존재하지 않습니다."));
//
//        if (!account.getUser().getId().equals(user.getId())) {
//            throw new IllegalArgumentException("계좌 접근 권한이 없습니다.");
//        }
//
//        return account;
//    }
}
