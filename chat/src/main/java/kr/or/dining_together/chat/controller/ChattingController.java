package kr.or.dining_together.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.ChattingMessage;
import kr.or.dining_together.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChattingController {
	@Value(value = "${kafka.topic.chat.name}")
	private String chatTopic;

	private final ChatService chatService;

	@PostMapping("/room")
	public ChatRoom createRoom(@RequestParam String name){
		return chatService.createRoom(name);
	}

	@GetMapping("/room")
	public List<ChatRoom> findAllRoom(){
		return chatService.findAllRoom();
	}

}
