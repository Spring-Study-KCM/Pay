package org.example.pay.friend.dto;

import org.example.pay.member.domain.Member;

public record FriendResponse(
	Long id,
	String name,
	String email
) {
	public static FriendResponse from(Member member) {
		return new FriendResponse(
			member.getId(),
			member.getName(),
			member.getEmail()
		);
	}
}
