package org.example.pay.friend.domain;

import org.example.pay.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "friend_id")
	private Member friend;

	private Friend(Member member, Member friend) {
		this.name = friend.getName();
		this.member = member;
		this.friend = friend;
	}

	public static Friend newFriend(Member member, Member friend) {
		return new Friend(member, friend);
	}
}
