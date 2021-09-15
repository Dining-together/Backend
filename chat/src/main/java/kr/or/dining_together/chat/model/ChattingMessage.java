package kr.or.dining_together.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingMessage {
	public enum MessageType{
		ENTER,TALK
	}
	private MessageType type;
	private String message;
	private String sender;
	private Long timeStamp;
	private String roomId;


	private ChattingMessage(){

	}
	public ChattingMessage(String message, String sender) {
		this.sender = sender;
		this.message = message;
	}

	public ChattingMessage(String message) {
		this.message = message;
	}
}
