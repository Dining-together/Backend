package kr.or.dining_together.chat.config.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.chat.model.ChatRoom;
import kr.or.dining_together.chat.model.ChattingMessage;
import kr.or.dining_together.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketChattingHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final ChatService chatService;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload=message.getPayload();
		log.info("#1.payload {}",payload);

		// TextMessage textMessage =new TextMessage("chatting 서버 접속 테스트");
		// session.sendMessage(textMessage);

		ChattingMessage chatMessage=objectMapper.readValue(payload,ChattingMessage.class);
		ChatRoom room=chatService.findRoomById(chatMessage.getRoomId());
		room.handleActions(session,chatMessage,chatService);

	}

	@EnableWebSocket
	@Configuration
	@RequiredArgsConstructor
	public class WebSocketConfig implements WebSocketConfigurer{
		private final WebSocketHandler webSocketHandler;

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			registry.addHandler(webSocketHandler,"/ws/chat").setAllowedOrigins("*");
		}
	}
}
