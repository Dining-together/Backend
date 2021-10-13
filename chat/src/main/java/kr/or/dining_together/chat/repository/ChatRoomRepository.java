package kr.or.dining_together.chat.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.UserIdDto;
import lombok.RequiredArgsConstructor;


public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

	@Query("select c from ChatRoom c where c.id=:id")
	Optional<ChatRoom> findById(@Param("id") String id);
	List<ChatRoom> findChatRoomsByCustomer(UserIdDto customer);
	List<ChatRoom> findChatRoomsByStore(UserIdDto store);
}