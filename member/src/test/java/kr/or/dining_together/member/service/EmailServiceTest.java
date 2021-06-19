package kr.or.dining_together.member.service;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.EmailInfo;
import kr.or.dining_together.member.jpa.repo.EmailInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmailServiceTest {

	@Autowired
	EmailInfoRepository emailInfoRepository;

	@Autowired
	EmailService emailService;

	@Test
	public void mailSendAndCheckTest() {
		//given
		String email = "qja9605@naver.com";
		String notTypedEmail = "qja9605@google.com";

		//when
		emailService.sendAuthMail(email);
		Optional<EmailInfo> emailInfo = emailInfoRepository.findByEmail(email);
		String key = emailInfo.get().getEmailKey();
		Optional<EmailInfo> emailInfo1 = emailInfoRepository.findByEmailKey(key);
		Optional<EmailInfo> emailInfo2 = emailInfoRepository.findByEmail(notTypedEmail);

		//then
		emailService.checkEmailExistence(notTypedEmail);
		emailService.checkEmailVerificationKey(email, key);
	}
}
