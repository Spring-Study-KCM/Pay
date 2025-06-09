package org.example.pay.user.service;

import java.util.List;

import org.example.pay.global.dto.CursorPage;
import org.example.pay.global.dto.CursorPageRequest;
import org.example.pay.user.dto.response.FriendInfoListResponse;
import org.example.pay.user.dto.response.FriendInfoResponse;
import org.example.pay.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public FriendInfoListResponse getFriend(CursorPageRequest request, Long userId) {
		List<FriendInfoResponse> friendInfo = userRepository.getFriendInfo(userId, request);
		CursorPage<FriendInfoResponse> friendInfoResponseCursorPage = CursorPage.of(friendInfo, request.size(),
			FriendInfoResponse::userId);
		int totalFriendCounts = userRepository.getTotalFriendCounts(userId).intValue();

		return FriendInfoListResponse.of(totalFriendCounts, friendInfoResponseCursorPage);
	}
}
