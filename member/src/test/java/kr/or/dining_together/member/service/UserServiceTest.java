package kr.or.dining_together.member.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
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
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.SignUpRequest;
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
			.email("qja9605@naver.com")
			.name("신태범")
			.password("1234")
			.roles(roles)
			.build();

		SignUpRequest signUpRequest = SignUpRequest.builder()
			.userDto(userDto)
			.userType(UserType.CUSTOMER)
			.age(24)
			.gender("MALE")
			.build();

		//when
		userService.save(signUpRequest);
		Optional<User> user = userRepository.findByEmail("qja9605@naver.com");

		//then
		System.out.println(user);
		assertEquals(user.get().getEmail(), "qja9605@naver.com");
	}

}
