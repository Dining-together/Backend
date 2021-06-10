package kr.or.dining_together.member.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.dto.SignUserDto;
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
		SignUserDto signUserDto = SignUserDto.builder()
			.email("qja9605@naver.com")
			.name("신태범")
			.password("1234")
			.build();

		SignUpRequest signUpRequest = SignUpRequest.builder()
			.signUserDto(signUserDto)
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
