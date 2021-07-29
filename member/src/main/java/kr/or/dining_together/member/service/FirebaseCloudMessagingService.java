package kr.or.dining_together.member.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;

import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserDeviceToken;
import kr.or.dining_together.member.jpa.repo.UserDeviceTokenRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.TokenMessageRequest;
import kr.or.dining_together.member.vo.TopicMessageRequest;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessagingService {

	private final String SEND_API_URL = "https://fcm.googleapis.com/v1/projects/diningtogether-edbd0/messages:send";
	private final ObjectMapper objectMapper;

	private final UserDeviceTokenRepository userDeviceTokenRepository;
	private final UserRepository userRepository;

	public void sendMessageByToken(String email, TokenMessageRequest tokenMessageRequest) throws
		Throwable {

		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByUser(user);

		Notification notification = new Notification(tokenMessageRequest.getTitle(), tokenMessageRequest.getBody());
		Message message = Message.builder()
			.setNotification(notification)
			.setToken(userDeviceToken.getToken())
			.build();

		sendMessage(message);
	}

	public void sendMessageByTopic(TopicMessageRequest topicMessageRequest) throws
		FirebaseMessagingException,
		IOException {
		Notification notification = new Notification(topicMessageRequest.getTitle(), topicMessageRequest.getBody());
		Message message = Message.builder()
			.setNotification(notification)
			.setTopic(topicMessageRequest.getTopic())
			.build();

		sendMessage(message);
	}

	private void sendMessage(Message message) throws IOException {
		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(String.valueOf(message),
			MediaType.get("application/json; charset=utf-8"));

		Request request = new Request.Builder()
			.url(SEND_API_URL)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.build();

		Response response = client.newCall(request)
			.execute();
	}

	private String getAccessToken() throws IOException {
		String firebaseConfigPath = "firebase/firebase_service_key.json";

		GoogleCredentials googleCredentials = GoogleCredentials
			.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
			.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

		googleCredentials.refreshIfExpired();
		return googleCredentials.getAccessToken().getTokenValue();
	}

	public void subscribeTopic(String email, String topic) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByUser(user);

		TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
			Collections.singletonList(userDeviceToken.getToken()), topic);
	}

	public void unsubscribeTopic(String email, String topic) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByUser(user);

		TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
			Collections.singletonList(userDeviceToken.getToken()), topic);
	}

	public void registerDeviceToken(String token, String email) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		UserDeviceToken userDeviceToken = UserDeviceToken.builder()
			.token(token)
			.user(user)
			.build();

		userDeviceTokenRepository.save(userDeviceToken);
	}
}