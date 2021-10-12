package kr.or.dining_together.chat.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.or.dining_together.chat.client.UserServiceClient;
import kr.or.dining_together.chat.model.ChatMessage;
import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.UserIdDto;
import kr.or.dining_together.chat.pubsub.RedisPublisher;
import kr.or.dining_together.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@CrossOrigin
@Transactional
public class ChattingController {
	private final RedisPublisher redisPublisher;
	private final UserServiceClient userServiceClient;
	private final ChatService chatService;

	/**
	 * websocket "/chat/pub/message"로 들어오는 메시징을 처리한다.
	 */
	@ApiOperation(value = "채팅방 메시지", notes = "메시지")
	@MessageMapping("/message")
	public void message(ChatMessage message) {
		// 채팅방 입장시에는 대화명과 메시지를 자동으로 세팅한다.
		log.info("채팅 메시지");
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			chatService.enterChatRoom(message.getRoomId());
			message.setSender("[알림]");
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		}else if(ChatMessage.MessageType.QUIT.equals(message.getType())){
			message.setSender("[알림]");
			message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
			chatService.deleteById(message.getRoomId());
		}
		chatService.save(message);
		// Websocket에 발행된 메시지를 redis로 발행(publish)
		ChannelTopic topic=chatService.getTopic(message.getRoomId());
		redisPublisher.publish(topic, message);
	}

}