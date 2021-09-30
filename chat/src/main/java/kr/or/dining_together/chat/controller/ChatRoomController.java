package kr.or.dining_together.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.or.dining_together.chat.client.UserServiceClient;
import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.ListResult;
import kr.or.dining_together.chat.model.SingleResult;
import kr.or.dining_together.chat.model.UserIdDto;
import kr.or.dining_together.chat.service.ChatService;
import kr.or.dining_together.chat.service.ResponseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

	private final ChatService chatService;
	private final ResponseService responseService;
	private final UserServiceClient userServiceClient;

	@ApiOperation(value = "room 전체 조회", notes = "채팅 룸 전체를 조회한다.")
	@GetMapping("/rooms")
	public ListResult<ChatRoom> rooms() {
		return responseService.getListResult(chatService.findAllRoom());
	}

	@ApiOperation(value = "채팅방 개설", notes = "채팅방을 개설한다.")
	@PostMapping("/room")
	public SingleResult<ChatRoom> createRoom(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,@RequestBody UserIdDto other) {
		UserIdDto me = userServiceClient.getUserId(xAuthToken);
		if(me.getType().equals("CUSTOMER")){
			return responseService.getSingleResult(chatService.createChatRoom(me,other));
		}else {
			return responseService.getSingleResult(chatService.createChatRoom(other, me));
		}
	}

	@ApiOperation(value = "방 정보 보기", notes = "방 정보")
	@GetMapping("/room/{roomId}")
	public SingleResult<ChatRoom> roomInfo(@PathVariable String roomId) {
		return responseService.getSingleResult(chatService.findRoomById(roomId));
	}

	@ApiOperation(value = "사용자 별 방 조회")
	@GetMapping("/user_room")
	public ListResult<ChatRoom> getRoomsByCustomer(@RequestHeader("X-AUTH-TOKEN") String xAuthToken){
		UserIdDto me=userServiceClient.getUserId(xAuthToken);
		return responseService.getListResult(chatService.getUserEnterRooms(me));
	}

}