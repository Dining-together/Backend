package kr.or.dining_together.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import kr.or.dining_together.chat.model.ChatMessage;
import kr.or.dining_together.chat.pubsub.RedisPublisher;
import kr.or.dining_together.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ChattingController {
	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;

	/**
	 * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
	 */
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			chatRoomRepository.enterChatRoom(message.getRoomId());
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		}
		// Websocket에 발행된 메시지를 redis로 발행한다(publish)
		redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
	}

}