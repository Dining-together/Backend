package kr.or.dining_together.member.vo;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
	private boolean validate_only;
	private Message message;

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Message{
		private Notification notification;
		private String token;
		private String topic;
	}

	@Builder
	@AllArgsConstructor
	@Getter
	public static  class Notification{
		private String title;
		private String body;
		private String image;
	}
}
