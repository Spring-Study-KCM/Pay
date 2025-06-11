package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 친구 요청 목록 응답 DTO (받은 요청)
@Getter
@AllArgsConstructor
public class FriendRequestResponse {
    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterEmail;
    private LocalDateTime createdAt;
}
