package org.example.pay.friend.repository;

import java.util.List;
import java.util.Optional;

import org.example.pay.friend.domain.Friend;
import org.example.pay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
	List<Friend> findByMember(Member member);

	Optional<Friend> findByMemberAndFriend(Member member, Member friend);

}
