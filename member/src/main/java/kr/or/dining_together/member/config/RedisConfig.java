package kr.or.dining_together.member.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

	// @Value("${spring.redis.host}")
	// private String redisHost;
	//
	// @Value("${spring.redis.port}")
	// private int redisPort;
	//
	// @Bean
	// public RedisConnectionFactory redisConnectionFactory() {
	// 	LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
	// 	return lettuceConnectionFactory;
	// }
	//
	// @Bean
	// public RedisTemplate<String, Object> redisTemplate() {
	// 	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	// 	redisTemplate.setConnectionFactory(redisConnectionFactory());
	// 	redisTemplate.setKeySerializer(new StringRedisSerializer());
	// 	return redisTemplate;
	// }
}