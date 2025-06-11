package org.example.repository;

import org.example.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend,Long> {
    // 내가 보낸 친구 요청 또는 친구 관계 조회
    @Query("SELECT f FROM Friend f JOIN FETCH f.friend WHERE f.user.id = :userId")
    List<Friend> findAllByUserIdFetchJoin(@Param("userId") Long userId);

    // 나에게 온 친구 요청 조회 (PENDING 상태만)
    @Query("SELECT f FROM Friend f JOIN FETCH f.user WHERE f.friend.id = :userId AND f.status = 'PENDING'")
    List<Friend> findPendingRequestsByFriendIdFetchJoin(@Param("userId") Long userId);

    // 특정 사용자간 친구 관계 조회
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    // 양방향 친구 관계 확인 (둘 중 하나라도 있으면 관계 존재)
    @Query("SELECT f FROM Friend f WHERE (f.user.id = :userId AND f.friend.id = :friendId) OR (f.user.id = :friendId AND f.friend.id = :userId)")
    List<Friend> findFriendshipBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);

    // 승인된 친구 목록만 조회
    @Query("SELECT f FROM Friend f JOIN FETCH f.friend WHERE f.user.id = :userId AND f.status = 'ACCEPTED'")
    List<Friend> findAcceptedFriendsByUserIdFetchJoin(@Param("userId") Long userId);
}
