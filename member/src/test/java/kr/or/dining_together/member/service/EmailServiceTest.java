package kr.or.dining_together.member.service;

import java.io.IOException;
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
	public void mailSendTest() throws IOException {
		//given
		String email = "qja9605@google.com";
		String notTypedEmail = "qja9605@naver.com";

		//when
		emailService.sendAuthMailBySendGrid(email);
	}
}
