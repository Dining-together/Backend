package kr.or.dining_together.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.chat.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

	List<ChatMessage> getChatMessagesByRoomId(String roomId);

}
