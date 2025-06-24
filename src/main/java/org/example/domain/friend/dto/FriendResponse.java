package org.example.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.friend.entity.Friend;
import org.example.global.constants.FriendStatus;

import java.time.LocalDateTime;

// 친구 목록 응답 DTO
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendResponse {
    private Long id;
    private Long friendId;
    private String friendName;
    private String friendEmail;
    private FriendStatus status;
    private LocalDateTime createdAt;
}
