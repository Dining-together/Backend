package kr.or.dining_together.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingMessage {
	private String message;
	private String user;
	private Long timeStamp;

	private String fileName;
	private String rawData;

	private ChattingMessage(){

	}
	public ChattingMessage(String message, String user) {
		this.user = user;
		this.message = message;
	}

	public ChattingMessage(String fileName, String rawData, String user) {

		this.fileName = fileName;
		this.rawData = rawData;
		this.user = user;
	}

	public ChattingMessage(String message) {
		this.message = message;
	}
}
