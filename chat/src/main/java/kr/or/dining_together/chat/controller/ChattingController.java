package kr.or.dining_together.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.dining_together.chat.model.ChattingMessage;
import kr.or.dining_together.chat.service.Receiver;
import kr.or.dining_together.chat.service.Sender;

@RestController
@CrossOrigin
public class ChattingController {
	@Value(value = "${kafka.topic.chat.name}")
	private String chatTopic;

	@Autowired
	private Sender sender;

	@Autowired
	private Receiver receiver;

	@Autowired
	private ChattingHistoryDAO chattingHistoryDAO;


	@MessageMapping("/message")
	public void sendMessage(ChattingMessage message) throws Exception{
		message.setTimeStamp(System.currentTimeMillis());
		chattingHistoryDAO.save(message);
		sender.send(chatTopic,message);
	}

	@RequestMapping("/history")
	public List<ChattingMessage> getChattingHistory() throws Exception{
		System.out.println("history!");
		return chattingHistoryDAO.get();
	}

	@MessageMapping("/file")
	@SendTo("/topic/chatting")
	public ChattingMessage sendFile(ChattingMessage message) throws Exception{
		return new ChattingMessage(message.getFileName(),message.getRawData(),message.getUser());
	}

}
