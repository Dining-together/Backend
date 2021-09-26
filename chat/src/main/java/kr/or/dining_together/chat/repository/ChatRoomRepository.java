package kr.or.dining_together.chat.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.UserIdDto;
import lombok.RequiredArgsConstructor;


public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

	List<ChatRoom> findChatRoomsByCustomer(UserIdDto customer);
	List<ChatRoom> findChatRoomsByStore(UserIdDto store);
}