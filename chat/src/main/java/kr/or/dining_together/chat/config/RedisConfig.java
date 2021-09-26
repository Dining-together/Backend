package kr.or.dining_together.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import kr.or.dining_together.chat.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

	/**
	 * 단일 topic 사용
	 */
	@Bean
	public ChannelTopic channelTopic(){
		return new ChannelTopic("chatroom");
	}

	/**
	 * redis pub/sub 메시지를 처리하는 listener 설정
	 */
	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory){
		RedisMessageListenerContainer container=new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}
	/**
	 * 실제 메시지를 처리하는 subscriber 설정 추가
	 */
	@Bean
	public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}

	/**
	 * redistTemplate
	 */
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		return redisTemplate;
	}
}
