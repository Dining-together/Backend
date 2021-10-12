package kr.or.dining_together.auction.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.TopicManagementResponse;

import kr.or.dining_together.auction.jpa.entity.UserDeviceToken;
import kr.or.dining_together.auction.jpa.repo.UserDeviceTokenRepository;
import kr.or.dining_together.auction.vo.FcmMessage;
import kr.or.dining_together.auction.vo.TokenMessageRequest;
import kr.or.dining_together.auction.vo.TopicMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseCloudMessagingService {

	private final String API_URL = "https://fcm.googleapis.com/v1/projects/diningtogether-ae422/messages:send";
	private final String FIREBASE_CONFIG_PATH = "firebase/firebase_service_key.json";
	private final ObjectMapper objectMapper;

	private final UserDeviceTokenRepository userDeviceTokenRepository;

	private FirebaseApp firebaseApp;

	@PostConstruct
	private void initialize() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();

			if (FirebaseApp.getApps().isEmpty()) {
				this.firebaseApp = FirebaseApp.initializeApp(options);
			} else {
				this.firebaseApp = FirebaseApp.getInstance();
			}
		} catch (IOException e) {
			log.error("Create FirebaseApp Error", e);
		}
	}

	public void registerDeviceToken(String token, String email) throws Throwable {
		UserDeviceToken userDeviceToken = UserDeviceToken.builder()
			.token(token)
			.email(email)
			.build();

		userDeviceTokenRepository.save(userDeviceToken);
	}

	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
		String message = makeMessage(targetToken, null,title, body);

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
			.url(API_URL)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
			.build();

		Response response = client.newCall(request)
			.execute();

		System.out.println(response.body().string());
	}

	public void sendMessageByToken(String email, TokenMessageRequest tokenMessageRequest) throws
		Throwable {

		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByEmail(email);

		String message = makeMessage(userDeviceToken.getToken(), null,tokenMessageRequest.getTitle(), tokenMessageRequest.getBody());

		sendMessage(message);
	}

	public void sendMessageByTopic(TopicMessageRequest topicMessageRequest) throws
		IOException {
		String message = makeMessage(null, topicMessageRequest.getTopic(),topicMessageRequest.getTitle(), topicMessageRequest.getBody());

		sendMessage(message);
	}

	private void sendMessage(String message) throws IOException {

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message,
			MediaType.get("application/json; charset=utf-8"));

		Request request = new Request.Builder()
			.url(API_URL)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
			.build();

		Response response = client.newCall(request)
			.execute();

		log.info(response.body().string());
	}

	private String getAccessToken() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials
			.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
			.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

		googleCredentials.refreshIfExpired();
		return googleCredentials.getAccessToken().getTokenValue();
	}

	public void subscribeTopic(String email, String topic) throws Throwable {
		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByEmail(email);

		TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
			Arrays.asList(userDeviceToken.getToken()), topic);

		log.info(response.getSuccessCount() + " tokens were subscribed successfully");
	}

	public void unsubscribeTopic(String email, String topic) throws Throwable {
		UserDeviceToken userDeviceToken = userDeviceTokenRepository.getUserDeviceTokenByEmail(email);

		TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
			Collections.singletonList(userDeviceToken.getToken()), topic);

		log.info(response.getSuccessCount() + " tokens were unsubscribed successfully");
	}

	private String makeMessage(String targetToken, String targetTopic,String title, String body) throws JsonProcessingException {
		FcmMessage fcmMessage = FcmMessage.builder()
			.message(FcmMessage.Message.builder()
				.token(targetToken)
				.topic(targetTopic)
				.notification(FcmMessage.Notification.builder()
					.title(title)
					.body(body)
					.image(null)
					.build()
				)
				.build()
			)
			.validate_only(false)
			.build();

		return objectMapper.writeValueAsString(fcmMessage);
	}
}