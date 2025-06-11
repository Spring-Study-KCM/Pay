package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.FriendRequest;
import org.example.dto.FriendRequestResponse;
import org.example.dto.FriendResponse;
import org.example.entity.Friend;
import org.example.entity.User;
import org.example.repository.FriendRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구 요청 보내기
    @Transactional
    public void sendFriendRequest(User user, FriendRequest request) {
        // 친구로 추가할 사용자 조회
        User friend = userRepository.findByEmailFetchJoin(request.getFriendEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 자기 자신에게 친구 요청 방지
        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }

        // 이미 친구 관계가 있는지 확인 (양방향)
        List<Friend> existingFriendship = friendRepository.findFriendshipBetweenUsers(user.getId(), friend.getId());
        if (!existingFriendship.isEmpty()) {
            throw new IllegalArgumentException("이미 친구 관계가 존재합니다.");
        }

        // 친구 요청 생성
        Friend friendRequest = Friend.builder()
                .user(user)
                .friend(friend)
                .status(Friend.FriendStatus.PENDING)
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
        if (friendRequest.getStatus() != Friend.FriendStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 친구 요청입니다.");
        }

        // 요청 수락
        friendRequest.setStatus(Friend.FriendStatus.ACCEPTED);

        // 양방향 친구 관계 생성 (역방향 관계도 생성)
        Friend reverseFriend = Friend.builder()
                .user(user)
                .friend(friendRequest.getUser())
                .status(Friend.FriendStatus.ACCEPTED)
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

    // 내 친구 목록 조회 (승인된 친구만)
    @Transactional(readOnly = true)
    public List<FriendResponse> getMyFriends(User user) {
        return friendRepository.findAcceptedFriendsByUserIdFetchJoin(user.getId()).stream()
                .map(f -> new FriendResponse(
                        f.getId(),
                        f.getFriend().getId(),
                        f.getFriend().getName(),
                        f.getFriend().getEmail(),
                        f.getStatus(),
                        f.getCreatedAt()
                ))
                .toList();
    }

    // 받은 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getPendingRequests(User user) {
        return friendRepository.findPendingRequestsByFriendIdFetchJoin(user.getId()).stream()
                .map(f -> new FriendRequestResponse(
                        f.getId(),
                        f.getUser().getId(),
                        f.getUser().getName(),
                        f.getUser().getEmail(),
                        f.getCreatedAt()
                ))
                .toList();
    }

    // 친구 삭제
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
