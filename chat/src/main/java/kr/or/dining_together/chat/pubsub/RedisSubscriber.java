package kr.or.dining_together.chat.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	/**
	 * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
	 */
	public void sendMessage(String publishMessage) {
		try {
			// ChatMessage 객채로 맵핑
			ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
			// 채팅방을 구독한 클라이언트에게 메시지 발송
			messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoom().getId(), chatMessage);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
	}
}
