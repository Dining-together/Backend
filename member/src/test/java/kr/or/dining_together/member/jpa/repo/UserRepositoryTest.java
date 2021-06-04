package kr.or.dining_together.member.jpa.repo;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

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
			.roles(Collections.singletonList("ROLE_USER"))
			.build());

		//when
		Optional<User> user = userRepository.findByEmail(email);
		System.out.println(user.get());
		System.out.println(modelMapper.map(user.get(), UserDto.class));
		System.out.println(modelMapper.map(modelMapper.map(user.get(), UserDto.class), User.class));

		//then
		assertNotNull(user);
		assertTrue(user.isPresent());
		assertEquals(user.get().getName(), name);

	}

}