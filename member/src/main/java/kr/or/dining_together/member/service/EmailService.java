package kr.or.dining_together.member.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private final JavaMailSender emailSender;
	private final EmailInfoRepository emailInfoRepository;
	private final UserRepository userRepository;

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
