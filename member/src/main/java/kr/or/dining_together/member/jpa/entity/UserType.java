package kr.or.dining_together.member.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
	USER("USER", "일반사용자"),
	SELLER("SELLER", "가게"),
	ADMIN("ADMIN", "관리자");

	private final String key;
	private final String title;
}
