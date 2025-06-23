package org.example.pay.account.pay_account.repository.custom;

import java.util.List;

import org.example.pay.account.pay_account.domain.QPayAccount;
import org.example.pay.account.pay_account.domain.QTransactions;
import org.example.pay.account.pay_account.dto.response.PayType;
import org.example.pay.account.pay_account.dto.response.TransactionsInfoResponse;
import org.example.pay.global.dto.CursorPageRequest;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionsRepositoryCustomImpl implements TransactionsRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<TransactionsInfoResponse> getTransactionsList(Long payAccountId, PayType payType, CursorPageRequest request) {
		QTransactions transactions = QTransactions.transactions;
		QPayAccount payAccount = QPayAccount.payAccount;

		BooleanExpression cursorCondition = null;
		if (request.cursorId() != null) {
			cursorCondition = transactions.id.lt(request.cursorId());
		}

		BooleanExpression payTypeCondition = null;

		if (payType != null) {
			payTypeCondition = transactions.payType.eq(payType);
		}

		return queryFactory
			.select(Projections.constructor(TransactionsInfoResponse.class,
				transactions.id,
				transactions.payAccount.id,
				transactions.payAccount.user.id,
				transactions.payType
			))
			.from(transactions)
			.join(transactions.payAccount, payAccount)
			.where(
				transactions.payAccount.id.eq(payAccountId),
				payTypeCondition,
				cursorCondition
			)
			.orderBy(transactions.id.desc())
			.limit(request.size() + 1L)
			.fetch();
	}

	@Override
	public Long getTotalTransactionsList(Long payAccountId, PayType payType) {
		QTransactions transactions = QTransactions.transactions;

		return queryFactory
			.select(transactions.count())
			.from(transactions)
			.where(
				transactions.payAccount.id.eq(payAccountId),
				transactions.payType.eq(payType)
			)
			.fetchOne();
	}
}
