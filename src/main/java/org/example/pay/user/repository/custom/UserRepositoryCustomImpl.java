package org.example.pay.user.repository.custom;

import static org.example.pay.user.domain.QUser.*;

import java.util.List;

import org.example.pay.global.dto.CursorPageRequest;
import org.example.pay.user.domain.QFriend;
import org.example.pay.user.dto.response.FriendInfoResponse;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<FriendInfoResponse> getFriendInfo(Long userId, CursorPageRequest request) {
		QFriend friend = QFriend.friend;

		BooleanExpression cursorCondition = null;
		if (request.cursorId() != null) {
			cursorCondition = friend.id.lt(request.cursorId());
		}

		return queryFactory
			.select(Projections.constructor(FriendInfoResponse.class,
				friend.friendUser.id,
				friend.friendUser.email
			))
			.from(friend)
			.join(friend.friendUser, user)
			.where(
				friend.user.id.eq(userId),
				cursorCondition
			)
			.orderBy(friend.id.asc())
			.limit(request.size() + 1L)
			.fetch();
	}

	@Override
	public Long getTotalFriendCounts(Long userId) {
		QFriend friend = QFriend.friend;

		return queryFactory
			.select(friend.count())
			.from(friend)
			.where(
				friend.user.id.eq(userId)
			)
			.fetchOne();
	}
}
