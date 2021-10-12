package kr.or.dining_together.auction.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PushNotificationResponse {
	private int status;
	private String message;
}
