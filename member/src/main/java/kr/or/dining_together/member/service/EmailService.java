package kr.or.dining_together.member.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

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

	public void sendVerificationMail(String to) {
		UUID emailKey = UUID.randomUUID();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("[From 회식모아] 이메일 인증");
		message.setText("인증번호는 " + emailKey + " 입니다");

		redisUtil.setDataExpire(emailKey.toString(), to, 60 * 30L);
		emailSender.send(message);
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
	public void sendUserPassword(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		String newPassword = makeRandomKey();
		SimpleMailMessage message = new SimpleMailMessage();

		userRepository.updatePassword(passwordEncoder.encode(newPassword), email);
		message.setTo(email);
		message.setSubject("[From 회식모아] 비밀번호 찾기");
		message.setText("귀하의 비밀번호는 " + newPassword + " 입니다");

		emailSender.send(message);
	}

	private String makeRandomKey() {
		Random r = new Random();
		return Integer.toString((r.nextInt(4589362) + 49311));
	}
}
