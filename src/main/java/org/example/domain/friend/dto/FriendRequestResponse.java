package org.example.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 친구 요청 목록 응답 DTO (받은 요청)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestResponse {
    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterEmail;
    private LocalDateTime createdAt;
}
