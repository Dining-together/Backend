package kr.or.dining_together.chat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatRoom implements Serializable {

	@Id
	@Column(name = "chatroom_id")
	private String id;

	private static final long serialVersionUID = 6494678977089006639L;

	private String name;

	private UserIdDto customer;

	private UserIdDto store;

	@OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ChatMessage> chatMessages = new ArrayList<>();

	public static ChatRoom create(String name,UserIdDto customer,UserIdDto store) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.id = UUID.randomUUID().toString();
		chatRoom.name = name;
		chatRoom.customer=customer;
		chatRoom.store=store;
		return chatRoom;
	}

	public void addChatMessages(ChatMessage chatMessage) {
		this.chatMessages.add(chatMessage);
	}

}
