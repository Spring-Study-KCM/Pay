package org.example.domain.friend.dto;

import lombok.Getter;

// 친구 요청 DTO
@Getter
public class FriendRequest {private Long id;
    private String friendEmail; // 친구로 추가할 사용자의 이메일
}
