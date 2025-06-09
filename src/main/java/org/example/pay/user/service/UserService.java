package org.example.pay.user.service;

import java.util.List;

import org.example.pay.global.dto.CursorPage;
import org.example.pay.global.dto.CursorPageRequest;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.UserErrorCode;
import org.example.pay.user.domain.Friend;
import org.example.pay.user.domain.User;
import org.example.pay.user.dto.request.AddFriendRequest;
import org.example.pay.user.dto.response.FriendInfoListResponse;
import org.example.pay.user.dto.response.FriendInfoResponse;
import org.example.pay.user.repository.FriendRepository;
import org.example.pay.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final FriendRepository friendRepository;

	public FriendInfoListResponse getFriend(CursorPageRequest request, Long userId) {
		List<FriendInfoResponse> friendInfo = userRepository.getFriendInfo(userId, request);
		CursorPage<FriendInfoResponse> friendInfoResponseCursorPage = CursorPage.of(friendInfo, request.size(),
			FriendInfoResponse::userId);
		int totalFriendCounts = userRepository.getTotalFriendCounts(userId).intValue();

		return FriendInfoListResponse.of(totalFriendCounts, friendInfoResponseCursorPage);
	}

	@Transactional
	public void addFriend(Long userId, AddFriendRequest addFriendRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new PayException(UserErrorCode.NOT_FOUND_USER));

		User friendUser = userRepository.findByEmail(addFriendRequest.email())
			.orElseThrow(() -> new PayException(UserErrorCode.NOT_FOUND_USER));

		Friend myFriend = Friend.builder()
			.user(user)
			.friendUser(friendUser)
			.build();

		Friend yourFriend = Friend.builder()
			.user(friendUser)
			.friendUser(user)
			.build();

		friendRepository.save(myFriend);
		friendRepository.save(yourFriend);
	}
}
