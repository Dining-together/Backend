package kr.or.dining_together.member.jpa.repo;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.EmailInfo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmailInfoRepositoryTest {

	@Autowired
	private EmailInfoRepository emailInfoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Test
	public void findByEmail() {
		String email = "qja9605@naver.com";
		String key = "1234";
		//given
		EmailInfo emailInfo = EmailInfo.builder()
			.emailKey(key)
			.email(email)
			.used(false)
			.build();

		//when
		System.out.println(emailInfo);
		emailInfoRepository.save(emailInfo);
		Optional<EmailInfo> emailInfo1 = emailInfoRepository.findByEmail(email);

		//then
		assertNotNull(emailInfo1.get());
		assertTrue(emailInfo1.isPresent());
		assertEquals(emailInfo1.get().getEmail(), email);
	}
}
