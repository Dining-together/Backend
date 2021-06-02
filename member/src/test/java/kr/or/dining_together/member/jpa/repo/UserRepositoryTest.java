package kr.or.dining_together.member.jpa.repo;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void findByEmail() {
		String email = "jifrozen@naver.com";
		String name = "문지언";
		//given
		userRepository.save(User.builder()
			.id(1L)
			.email(email)
			.name(name)
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.type(UserType.CUSTOMER)
			.roles(Collections.singletonList("ROLE_USER"))
			.build());
		//when
		Optional<User> user = userRepository.findByEmail(email);
		//then
		assertNotNull(user);
		assertTrue(user.isPresent());
		assertEquals(user.get().getName(), name);

	}

}