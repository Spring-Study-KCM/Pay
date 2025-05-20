package org.example.pay.account.constants;

public enum BankCode {
	KOOKMIN("00", "국민은행"),
	KAKAO("01", "카카오뱅크");

	private final String code;
	private final String name;

	BankCode(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
