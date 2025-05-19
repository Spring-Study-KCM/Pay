package org.example.pay.account.bank_account.dto.request;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.pay.global.exception.PayException;
import org.example.pay.global.exception.code.BankAccountErrorCode;

@Getter
@AllArgsConstructor
public enum BankType {

    KAKAO_BANK("카카오뱅크"),
    KOOKMIN_BANK("국민은행"),
    SHINHAN_BANK("신한은행");

    private final String name;

    public static BankType getBankType(String bankName) {
        if (bankName == null) {
            throw new PayException(BankAccountErrorCode.BANK_NAME_NOT_NULL);
        }

        return Arrays.stream(BankType.values())
                .filter(type -> type.getName().equals(bankName))
                .findFirst()
                .orElseThrow(() -> new PayException(BankAccountErrorCode.NOT_FOUND_BANK_NAME));
    }
}