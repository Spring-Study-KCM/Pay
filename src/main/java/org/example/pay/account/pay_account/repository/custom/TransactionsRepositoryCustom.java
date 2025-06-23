package org.example.pay.account.pay_account.repository.custom;

import java.util.List;

import org.example.pay.account.pay_account.dto.response.PayType;
import org.example.pay.account.pay_account.dto.response.TransactionsInfoResponse;
import org.example.pay.global.dto.CursorPageRequest;

public interface TransactionsRepositoryCustom {

	List<TransactionsInfoResponse> getTransactionsList(Long payAccountId, PayType payType, CursorPageRequest request);

	Long getTotalTransactionsList(Long payAccountId, PayType payType);

}
