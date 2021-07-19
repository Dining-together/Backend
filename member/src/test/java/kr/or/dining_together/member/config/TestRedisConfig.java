package kr.or.dining_together.member.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
public class TestRedisConfig {
	private RedisServer redisServer;

	public TestRedisConfig(@Value("${spring.redis.port}") int redisPort) {
		redisServer = new RedisServer(redisPort);
	}

	@PostConstruct
	public void startRedis() throws IOException {
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}
}

