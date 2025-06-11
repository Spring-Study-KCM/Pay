package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Friend;

import java.time.LocalDateTime;

// 친구 목록 응답 DTO
@Getter
@AllArgsConstructor
public class FriendResponse {
    private Long id;
    private Long friendId;
    private String friendName;
    private String friendEmail;
    private Friend.FriendStatus status;
    private LocalDateTime createdAt;
}
