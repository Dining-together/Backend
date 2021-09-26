package kr.or.dining_together.chat.service;



import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import kr.or.dining_together.chat.model.ChatMessage;
import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.UserIdDto;
import kr.or.dining_together.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	// Redis CacheKeys
	private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
	public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

	private HashOperations<String, String, String> hashOpsEnterInfo;
	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	@PostConstruct
	private void init(){
		/** 1. redisTemplate에서 operation 받기 **/
		hashOpsEnterInfo = redisTemplate.opsForHash();
	}
	public List<ChatRoom> findAllRoom() {
		return chatRoomRepository.findAll();
	}

	public ChatRoom findRoomById(String id) {
		ChatRoom chatRoom=(ChatRoom)chatRoomRepository.findById(id).orElseThrow();
		return chatRoom;
	}

	/**
	 * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
	 */
	public ChatRoom createChatRoom(UserIdDto customer, UserIdDto store) {
		String name=customer.getName()+"와 "+store.getName();
		ChatRoom chatRoom = ChatRoom.create(name,customer,store);
		chatRoomRepository.save(chatRoom);
		return chatRoom;
	}

	public List<ChatRoom> getCustomerEnterRooms(UserIdDto customer){
		return chatRoomRepository.findChatRoomsByCustomer(customer);
	}
	public List<ChatRoom> getStoreEnterRooms(UserIdDto store){
		return chatRoomRepository.findChatRoomsByStore(store);
	}

	public void deleteById(ChatRoom chatRoom){
		chatRoomRepository.delete(chatRoom);
	}

	/**
	 * destination정보에서 roomId 추출
	 */
	public String getRoomId(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if (lastIndex != -1)
			return destination.substring(lastIndex + 1);
		else
			return "";
	}

	/**
	 * 채팅방에 메시지 발송
	 */
	public void sendChatMessage(ChatMessage chatMessage) {
		if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
			chatMessage.setSender("[알림]");
		} else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
			chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
			chatMessage.setSender("[알림]");
		}
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
	}
	// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
	public void setUserEnterInfo(String sessionId, String roomId) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
	}

	// 유저 세션으로 입장해 있는 채팅방 ID 조회
	public String getUserEnterRoomId(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}

	// 유저 세션정보와 맵핑된 채팅방ID 삭제
	public void removeUserEnterInfo(String sessionId) {
		hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
	}

}