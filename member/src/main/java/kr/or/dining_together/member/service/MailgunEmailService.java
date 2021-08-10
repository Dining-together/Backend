package kr.or.dining_together.member.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {

	@Value(value = "${mailgun.domain.name}")
	private String YOUR_DOMAIN_NAME;

	@Value(value = "${mailgun.api}")
	private String API_KEY;

	private final UserRepository userRepository;
	private final RedisUtil redisUtil;
	private final PasswordEncoder passwordEncoder;

	public void checkEmailExistence(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			throw new UserDuplicationException();
		}
		return;
	}

	public void sendVerificationMail(String receiver) throws UnirestException {
		String emailKey = makeRandomKey();
		redisUtil.setDataExpire(emailKey, receiver, 60 * 30L);

		String subject = "[From 회식모아] 이메일 인증";
		String text = "인증번호는 " + emailKey + " 입니다";

		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.queryString("from", "회식모아 <moamoa202105@gmail.com>")
			.queryString("to", receiver)
			.queryString("subject", subject)
			.queryString("text", text)
			.asJson();

		System.out.println(request.getBody());

		return;
	}

	public void verifyEmail(String email, String key) {
		String verifiedEmail = redisUtil.getData(key);
		if (verifiedEmail == null) {
			throw new VerificationFailedException();
		} else if (verifiedEmail.equals(email) == false) {
			throw new VerificationFailedException();
		}

		redisUtil.deleteData(key);
		return;
	}

	@Transactional
	public void sendUserPassword(String receiver) throws UnirestException {
		Optional<User> user = userRepository.findByEmail(receiver);
		String newPassword = makeRandomKey();
		SimpleMailMessage message = new SimpleMailMessage();

		userRepository.updatePassword(passwordEncoder.encode(newPassword), receiver);

		String subject = "[From 회식모아] 비밀번호 찾기";
		String text = "귀하의 비밀번호는 " + newPassword + " 입니다";

		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.queryString("from", "회식모아 <moamoa202105@gmail.com>")
			.queryString("to", receiver)
			.queryString("subject", subject)
			.queryString("text", text)
			.asJson();

		System.out.println(request.getBody());

		if (user.isPresent()) {
			throw new UserDuplicationException();
		}
		return;
	}

	private String makeRandomKey() {
		Random r = new Random();
		return Integer.toString((r.nextInt(4589362) + 49311));
	}
}
