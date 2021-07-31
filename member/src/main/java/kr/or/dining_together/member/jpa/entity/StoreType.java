package kr.or.dining_together.member.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreType {
	KOREAN(Values.KOREAN),
	CHINESE(Values.CHINESE),
	JAPANESE(Values.JAPANESE),
	WESTERN(Values.WESTERN);

	private String value;

	StoreType(String val) {
		if (!this.name().equals(val)) {
			throw new IllegalArgumentException("Incorrect use of StoreType");
		}
	}

	public static class Values {
		public static final String KOREAN = "KOREAN";
		public static final String CHINESE = "CHINESE";
		public static final String JAPANESE = "JAPANESE";
		public static final String WESTERN = "WESTERN";
	}
}
