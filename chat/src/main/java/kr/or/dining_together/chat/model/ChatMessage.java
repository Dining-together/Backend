package kr.or.dining_together.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private MessageType type; // 메시지 타입
	private String sender; // 메시지 보낸사람
	private String message; // 메시지
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JoinColumn(name = "chatroom_id")
	private ChatRoom chatRoom;

	public static ChatMessage createChatMessage(ChatRoom chatRoom, String sender, String message,MessageType type) {
		ChatMessage chatMessage= ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.message(message)
			.type(type)
			.build();
		return chatMessage;
	}

	public void setSender(String sender){
		this.sender=sender;
	}

	public void setMessage(String message){
		this.message=message;
	}

	// 메시지 타입 : 입장, 퇴장, 채팅
	public enum MessageType {
		ENTER, QUIT, TALK
	}

}