package org.example.pay.user.dto.response;

import org.example.pay.global.dto.CursorPage;

import io.swagger.v3.oas.annotations.media.Schema;

public record FriendInfoListResponse(
	@Schema(description = "총 친구 수", example = "24")
	int friendCounts,

	CursorPage<FriendInfoResponse> friendInfoResponseCursorPage
) {
	public static FriendInfoListResponse of(int friendCounts, CursorPage<FriendInfoResponse> friendInfoResponseCursorPage) {
		return new FriendInfoListResponse(friendCounts, friendInfoResponseCursorPage);
	}
}