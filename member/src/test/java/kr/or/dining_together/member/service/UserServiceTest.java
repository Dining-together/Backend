package kr.or.dining_together.member.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;
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
	public void insertTest() {
		//given
		List<String> roles = Arrays.asList("USER");
		User user = User.builder()
			.id(1L)
			.email("qja9605@naver.com")
			.name("신태범")
			.password(passwordEncoder.encode("1234"))
			.phoneNo("010-2691-3895")
			.joinDate(new Date())
			.roles(roles)
			.build();

		//when
		userRepository.save(user);

		LoginRequest loginRequest = new LoginRequest("qja9605@naver.com", "1234");

		UserDto userDto = userService.login(loginRequest);
		//then

		System.out.println(user);
		System.out.println(userDto);
	}

	@Test
	public void updatePassword() {

		User user = User.builder()
			.id(1L)
			.email("jifrozen@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.phoneNo("010-1234-5678")
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();
		System.out.println(passwordEncoder.matches("test1111", user.getPassword()));
		userRepository.save(user);

	}

}
