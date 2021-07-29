package kr.or.dining_together.member.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.jpa.entity.EmailInfo;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.EmailInfoRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private static final String FROM_ADDRESS = "moamoa202105@gmail.com";
	private final JavaMailSender emailSender;
	private final EmailInfoRepository emailInfoRepository;
	private final UserRepository userRepository;

	static final String SENDGRID_API_KEY = "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";
	static final String SENDGRID_SENDER = "qja9605@naver.com";

	public void sendAuthMailBySendGrid(String receiver) throws IOException {
		String key = makeRandomKey();
		EmailInfo emailInfo = EmailInfo.builder()
			.key(key)
			.email(receiver)
			.used(false)
			.build();

		Long savedEmailId = emailInfoRepository.save(emailInfo).getId();

		Email from = new Email(SENDGRID_SENDER);
		String subject = "[From 회식모아] 이메일 인증";
		Email to = new Email(receiver);
		// Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		String contentValue="인증번호는"+key+"입니다";
		Content content = new Content("text/plain",contentValue);
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}

		if (savedEmailId == null) {
			throw new DataSaveFailedException();
		}
		return;
	}
	@Transactional
	@Async
	public void sendAuthMail(String to) {
		String key = makeRandomKey();
		EmailInfo emailInfo = EmailInfo.builder()
			.key(key)
			.email(to)
			.used(false)
			.build();

		Long savedEmailId = emailInfoRepository.save(emailInfo).getId();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("[From 회식모아] 이메일 인증");
		message.setText("인증번호는 " + key + " 입니다");
		emailSender.send(message);

		if (savedEmailId == null) {
			throw new DataSaveFailedException();
		}
		return;
	}

	public void checkEmailVerificationKey(String email, String key) {
		Optional<EmailInfo> emailInfoByKey = emailInfoRepository.findByKey(key);
		Optional<EmailInfo> emailInfoByEmail = emailInfoRepository.findByEmail(email);
		if (emailInfoByKey.isEmpty() || emailInfoByEmail.isEmpty()) {
			throw new VerificationFailedException();
		}
		if (emailInfoByKey.get().getId() != emailInfoByEmail.get().getId()) {
			throw new VerificationFailedException();
		}
		return;
	}

	public void checkEmailExistence(String email) {
		Optional<User> user = userRepository.findByEmail(email);

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
