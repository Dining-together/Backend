package kr.or.dining_together.member.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendgridEmailService {

	static final String SENDGRID_API_KEY = "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";
	static final String SENDGRID_SENDER = "qja9605@naver.com";
	private static final String FROM_ADDRESS = "moamoa202105@gmail.com";
	private final JavaMailSender emailSender;
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

	public void sendVerificationMail(String receiver) throws IOException {
		String key = makeRandomKey();
		redisUtil.setDataExpire(key.toString(), receiver, 60 * 30L);

		Email from = new Email(SENDGRID_SENDER);
		String subject = "[From 회식모아] 이메일 인증";
		Email to = new Email(receiver);
		// Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		String contentValue = "인증번호는" + key + "입니다";
		sendEmail(from, subject, to, contentValue);

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
	public void sendUserPassword(String email) throws IOException {
		Optional<User> user = userRepository.findByEmail(email);
		String newPassword = makeRandomKey();
		SimpleMailMessage message = new SimpleMailMessage();

		userRepository.updatePassword(passwordEncoder.encode(newPassword), email);

		Email from = new Email(SENDGRID_SENDER);
		String subject = "[From 회식모아] 비밀번호 찾기";
		Email to = new Email(email);
		// Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		String contentValue = "귀하의 비밀번호는 " + newPassword + " 입니다";
		sendEmail(from, subject, to, contentValue);

		if (user.isPresent()) {
			throw new UserDuplicationException();
		}
		return;
	}

	private void sendEmail(Email from, String subject, Email to, String contentValue) throws IOException {
		Content content = new Content("text/plain", contentValue);
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			// System.out.println(response.getStatusCode());
			// System.out.println(response.getBody());
			// System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	private String makeRandomKey() {
		Random r = new Random();
		return Integer.toString((r.nextInt(4589362) + 49311));
	}
}
