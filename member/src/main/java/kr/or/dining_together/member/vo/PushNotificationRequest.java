package kr.or.dining_together.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PushNotificationRequest {
	private String title;
	private String message;
	private String topic;
	private String token;
}
