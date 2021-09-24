package kr.or.dining_together.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.ListResult;
import kr.or.dining_together.chat.model.SingleResult;
import kr.or.dining_together.chat.repository.ChatRoomRepository;
import kr.or.dining_together.chat.service.ResponseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

	private final ChatRoomRepository chatRoomRepository;
	private final ResponseService responseService;


	@GetMapping("/rooms")
	public ListResult<ChatRoom> rooms() {
		return responseService.getListResult(chatRoomRepository.findAllRoom());
	}

	@PostMapping("/room")
	public SingleResult<ChatRoom> createRoom(@RequestParam String name) {
		return responseService.getSingleResult(chatRoomRepository.createChatRoom(name));
	}

	@GetMapping("/room/{roomId}")
	public SingleResult<ChatRoom> roomInfo(@PathVariable String roomId) {
		return responseService.getSingleResult(chatRoomRepository.findRoomById(roomId));
	}
}