package kr.or.dining_together.member.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
	CUSTOMER(Values.CUSTOMER),
	STORE(Values.STORE),
	ADMIN(Values.ADMIN);

	private String value;

	UserType(String val) {
		if (!this.name().equals(val)) {
			throw new IllegalArgumentException("Incorrect use of UserType");
		}
	}

	public static class Values {
		public static final String CUSTOMER = "CUSTOMER";
		public static final String STORE = "STORE";
		public static final String ADMIN = "ADMIN";
	}
}
