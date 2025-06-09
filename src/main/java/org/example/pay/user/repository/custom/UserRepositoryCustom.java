package org.example.pay.user.repository.custom;

import java.util.List;

import org.example.pay.global.dto.CursorPageRequest;
import org.example.pay.user.dto.response.FriendInfoResponse;

public interface UserRepositoryCustom {

	List<FriendInfoResponse> getFriendInfo(
		Long userId,
		CursorPageRequest request
	);

	Long getTotalFriendCounts(
		Long userId
	);
}
