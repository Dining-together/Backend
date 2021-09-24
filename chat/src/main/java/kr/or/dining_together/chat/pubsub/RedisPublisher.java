package kr.or.dining_together.chat.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import kr.or.dining_together.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

	private final RedisTemplate<String,Object> redisTemplate;

	public void publish(ChannelTopic topic, ChatMessage chattingMessage){
		redisTemplate.convertAndSend(topic.getTopic(),chattingMessage);
	}
}
