package kr.or.dining_together.member.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.CustomerSignUpRequest;
import kr.or.dining_together.member.vo.StoreSignUpRequest;
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
	public void customerSignUpTest() {
		//given
		CustomerSignUpRequest signUpRequest = CustomerSignUpRequest.builder()
			.email("qja96051@naver.com")
			.password("1234")
			.name("하ㄹ하")
			.age(24)
			.gender("MALE")
			.build();

		//when
		userService.customerSave(signUpRequest);
		Optional<User> user = userRepository.findByEmail("qja96051@naver.com");

		//then
		System.out.println(user);
		assertEquals(user.get().getEmail(), "qja96051@naver.com");
	}

	@Test
	public void storeSignUpTest() {
		//given
		StoreSignUpRequest storeSignUpRequest = StoreSignUpRequest.builder()
			.email("qja9605@naver.com")
			.name("신태범")
			.password("1234")
			.build();

		//when
		userService.storeSave(storeSignUpRequest);
		Optional<User> user = userRepository.findByEmail("qja9605@naver.com");

		//then
		System.out.println(user);
		assertEquals(user.get().getEmail(), "qja9605@naver.com");
	}

}
