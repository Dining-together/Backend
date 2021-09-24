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
public class RedisSubscriber implements MessageListener {
	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;
	private final SimpMessageSendingOperations messagingTemplate;
	/**
	 * Redis 에서 메시지 publish 대기하고 있던 onMessage 해당 메시지 받아 처리
	 */
	@Override
	public void onMessage(Message message, byte[] bytes) {
		try{
			// redis 데이터 받아 deserialize
			String publishMessage= (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

			ChatMessage roomMessage =objectMapper.readValue(publishMessage, ChatMessage.class);

			messagingTemplate.convertAndSend("/sub/chat/room"+roomMessage.getRoomId(),roomMessage);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
