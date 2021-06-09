package kr.or.dining_together.auction.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
	PROCEEDING("PROCEEDING", "진행중"),
	END("END", "마감"),
	CANCEL("CANCEL", "취소");

	private final String key;
	private final String title;
}