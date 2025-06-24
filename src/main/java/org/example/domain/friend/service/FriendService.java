package org.example.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.friend.dto.FriendRequest;
import org.example.domain.friend.dto.FriendRequestResponse;
import org.example.domain.friend.dto.FriendResponse;
import org.example.domain.friend.entity.Friend;
import org.example.domain.user.entity.User;
import org.example.domain.friend.repository.FriendRepository;
import org.example.domain.user.repository.UserRepository;
import org.example.global.constants.FriendStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구 요청 보내기
    @Transactional // 쓰기 작업이므로 readOnly 오버라이드
    public void sendFriendRequest(User user, FriendRequest request) {
        // 친구로 추가할 사용자 조회
        User friend = userRepository.findByEmailFetchJoin(request.getFriendEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 자기 자신에게 친구 요청 방지
        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }

        // 이미 친구 관계가 있는지 확인 (양방향) - 피드백 3번: 쿼리 최적화
        List<Friend> existingFriendship = friendRepository.findFriendshipBetweenUsers(user.getId(), friend.getId());
        if (!existingFriendship.isEmpty()) {
            throw new IllegalArgumentException("이미 친구 관계가 존재합니다.");
        }

        // 친구 요청 생성
        Friend friendRequest = Friend.builder()
                .user(user)
                .friend(friend)
                .status(FriendStatus.PENDING)
                .build();

        friendRepository.save(friendRequest);
    }

    // 친구 요청 수락
    @Transactional
    public void acceptFriendRequest(User user, Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 존재하지 않습니다."));

        // 요청 받은 사람인지 확인
        if (!friendRequest.getFriend().getId().equals(user.getId())) {
            throw new IllegalArgumentException("친구 요청을 수락할 권한이 없습니다.");
        }

        // 이미 처리된 요청인지 확인
        if (friendRequest.getStatus() != FriendStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 친구 요청입니다.");
        }

        // 요청 수락
        friendRequest.setStatus(FriendStatus.ACCEPTED);

        // 양방향 친구 관계 생성 (역방향 관계도 생성)
        Friend reverseFriend = Friend.builder()
                .user(user)
                .friend(friendRequest.getUser())
                .status(FriendStatus.ACCEPTED)
                .build();

        friendRepository.save(reverseFriend);
    }

    // 친구 요청 거절
    @Transactional
    public void rejectFriendRequest(User user, Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 존재하지 않습니다."));

        // 요청 받은 사람인지 확인
        if (!friendRequest.getFriend().getId().equals(user.getId())) {
            throw new IllegalArgumentException("친구 요청을 거절할 권한이 없습니다.");
        }

        // 요청 삭제
        friendRepository.delete(friendRequest);
    }

    // 내 친구 목록 조회 (승인된 친구만) - 피드백 6번: Builder 패턴 적용
    public List<FriendResponse> getMyFriends(User user) {
        return friendRepository.findAcceptedFriendsByUserIdFetchJoin(user.getId()).stream()
                .map(f -> FriendResponse.builder()
                        .id(f.getId())
                        .friendId(f.getFriend().getId())
                        .friendName(f.getFriend().getName())
                        .friendEmail(f.getFriend().getEmail())
                        .status(f.getStatus())
                        .createdAt(f.getCreatedAt())
                        .build())
                .toList();
    }

    // 받은 친구 요청 목록 조회
    public List<FriendRequestResponse> getPendingRequests(User user) {
        return friendRepository.findPendingRequestsByFriendIdFetchJoin(user.getId()).stream()
                .map(f -> FriendRequestResponse.builder()
                        .id(f.getId())
                        .requesterId(f.getUser().getId())
                        .requesterName(f.getUser().getName())
                        .requesterEmail(f.getUser().getEmail())
                        .createdAt(f.getCreatedAt())
                        .build())
                .toList();
    }

    // 친구 삭제 - 피드백 7번: 양방향 연관관계 활용 고려 (현재는 단방향이므로 유지)
    @Transactional
    public void deleteFriend(User user, Long friendId) {
        // 내가 친구로 추가한 관계 찾기
        Friend myFriend = friendRepository.findByUserIdAndFriendId(user.getId(), friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 관계가 존재하지 않습니다."));

        // 상대방이 나를 친구로 추가한 관계 찾기
        Friend reverseFriend = friendRepository.findByUserIdAndFriendId(friendId, user.getId())
                .orElse(null);

        // 양방향 관계 모두 삭제
        friendRepository.delete(myFriend);
        if (reverseFriend != null) {
            friendRepository.delete(reverseFriend);
        }
    }
}
