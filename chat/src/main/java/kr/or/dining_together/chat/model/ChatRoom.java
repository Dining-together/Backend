package kr.or.dining_together.chat.model;

import java.io.Serializable;
import java.util.UUID;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Serializable {
	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;

	private String name;

	public static ChatRoom create(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = UUID.randomUUID().toString();
		chatRoom.name = name;
		return chatRoom;
	}

}
