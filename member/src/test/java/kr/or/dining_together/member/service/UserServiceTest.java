package kr.or.dining_together.member.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class UserServiceTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void signUpTest() {
		//given
		List<String> roles = Arrays.asList("USER");
		UserDto userDto = UserDto.builder()
			.id(1L)
			.email("qja9605@naver.com")
			.name("신태범")
			.password(passwordEncoder.encode("1234"))
			.joinDate(new Date())
			.roles(roles)
			.build();

		//when
		userService.save(userDto);
		Optional<User> user = userRepository.findByEmail("qja9605@naver.com");

		//then
		System.out.println(user);
	}

}
