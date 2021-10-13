package kr.or.dining_together.chat.controller;

import kr.or.dining_together.chat.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
	private String roomId;
	private ChatMessage.MessageType type; // 메시지 타입
	private String sender; // 메시지 보낸사람
	private String message; // 메시지
}
