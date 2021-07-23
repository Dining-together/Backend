package kr.or.dining_together.auction.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {
	PROCEEDING(Values.PROCEEDING),
	END(Values.END),
	CANCEL(Values.CANCEL);

	private String value;

	AuctionStatus(String val) {
		if (!this.name().equals(val)) {
			throw new IllegalArgumentException("Incorrect use of AuctionStatus");
		}
	}

	public static class Values {
		public static final String PROCEEDING = "PROCEEDING";
		public static final String END = "END";
		public static final String CANCEL = "CANCEL";
	}
}
