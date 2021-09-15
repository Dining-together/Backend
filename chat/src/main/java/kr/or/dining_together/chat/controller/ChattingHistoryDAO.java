package kr.or.dining_together.chat.controller;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import kr.or.dining_together.chat.model.ChattingMessage;

@Component
public class ChattingHistoryDAO {
	// A simple cache for temporarily storing chat data
	private final Cache<UUID, ChattingMessage> chatHistoryCache = CacheBuilder
		.newBuilder().maximumSize(20).expireAfterWrite(10, TimeUnit.MINUTES)
		.build();

	public void save(ChattingMessage chatObj) {
		this.chatHistoryCache.put(UUID.randomUUID(), chatObj);
	}

	public List<ChattingMessage> get() {
		return chatHistoryCache.asMap().values().stream()
			.sorted(Comparator.comparing(ChattingMessage::getTimeStamp))
			.collect(Collectors.toList());
	}
}