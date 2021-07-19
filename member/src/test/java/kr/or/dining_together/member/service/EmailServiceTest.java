package kr.or.dining_together.member.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmailServiceTest {

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private EmailService emailService;

	@Test
	public void mailSendAndCheckTest() {
		//given
		String email = "qja9605@naver.com";
		String notTypedEmail = "qja9605@google.com";

		//when
		emailService.sendVerificationMail(email);
		String token = redisUtil.getData(email);

		//then
		emailService.checkEmailExistence(notTypedEmail);
		emailService.verifyEmail(email, token);
	}

}
