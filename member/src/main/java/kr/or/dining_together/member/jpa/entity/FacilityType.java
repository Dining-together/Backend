package kr.or.dining_together.member.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FacilityType {
	ROOM(Values.ROOM),
	GROUPSEAT(Values.GROUPSEAT),
	SEDENTARY(Values.SEDENTARY),
	INTERNET(Values.INTERNET),
	HIGHCHAIR(Values.HIGHCHAIR),
	HANDICAP(Values.HANDICAP),
	PET(Values.PET);

	private String value;

	FacilityType(String val) {
		if (!this.name().equals(val)) {
			throw new IllegalArgumentException("Incorrect use of FacilityType");
		}
	}

	public static class Values {
		public static final String ROOM = "ROOM";
		public static final String GROUPSEAT = "GROUPSEAT";
		public static final String SEDENTARY = "SEDENTARY";
		public static final String INTERNET = "INTERNET";
		public static final String HIGHCHAIR = "HIGHCHAIR";
		public static final String HANDICAP = "HANDICAP";
		public static final String PET = "PET";
	}
}
