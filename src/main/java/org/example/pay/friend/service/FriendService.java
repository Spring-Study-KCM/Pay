package org.example.pay.friend.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.example.pay.friend.domain.Friend;
import org.example.pay.friend.dto.AddFriendRequest;
import org.example.pay.friend.dto.DeleteFriendRequest;
import org.example.pay.friend.dto.FriendResponse;
import org.example.pay.friend.repository.FriendRepository;
import org.example.pay.member.domain.Member;
import org.example.pay.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;

	public void addFriend(Long memberId, AddFriendRequest request) {
		Member me = getMemberOrThrow(memberId);
		Member friend = getMemberOrThrow(request.friendId());

		if (friendRepository.findByMemberAndFriend(me, friend).isPresent()) {
			throw new IllegalStateException("이미 친구입니다.");
		}

		Friend friendRelation = Friend.newFriend(me, friend);
		friendRepository.save(friendRelation);
	}

	public List<FriendResponse> getMyFriends(Long memberId) {
		Member me = getMemberOrThrow(memberId);
		return friendRepository.findByMember(me)
			.stream()
			.map(Friend::getFriend)
			.map(FriendResponse::from)
			.toList();
	}

	public void deleteFriend(Long memberId, DeleteFriendRequest request) {
		Member me = getMemberOrThrow(memberId);
		Member friend = getMemberOrThrow(request.friendId());

		Friend relation = friendRepository.findByMemberAndFriend(me, friend)
			.orElseThrow(() -> new NoSuchElementException("친구가 아닙니다."));

		friendRepository.deleteById(relation.getId());
	}

	private Member getMemberOrThrow(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
	}
}
