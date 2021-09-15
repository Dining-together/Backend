// package kr.or.dining_together.chat.service;
//
// import java.util.HashMap;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Service;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import kr.or.dining_together.chat.model.ChattingMessage;
//
// @Service
// public class Receiver {
// 	private static final Logger LOGGER= LoggerFactory.getLogger(Receiver.class);
// 	@Value(value = "${kafka.topic.chat.name}")
// 	private String chatTopic;
// 	@Value(value = "${kafka.topic.chat.id}")
// 	private String chatId;
//
// 	@Autowired
// 	private SimpMessagingTemplate template;
//
// 	@KafkaListener(id="chatId",topics="chat-topic")
// 	public void receive(ChattingMessage message) throws Exception{
// 		LOGGER.info("message='{}'",message);
// 		HashMap<String,String> msg=new HashMap<>();
// 		msg.put("timestamp",Long.toString(message.getTimeStamp()));
// 		msg.put("message",message.getMessage());
// 		msg.put("author",message.getUser());
//
// 		ObjectMapper mapper=new ObjectMapper();
// 		String json=mapper.writeValueAsString(msg);
//
// 		this.template.convertAndSend("/topic/public",json);
// 	}
//
// }
