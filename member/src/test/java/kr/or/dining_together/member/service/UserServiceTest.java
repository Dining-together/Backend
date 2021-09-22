package kr.or.dining_together.member.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.dto.UserIdDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.CustomerProfileRequest;
import kr.or.dining_together.member.vo.CustomerProfileResponse;
import kr.or.dining_together.member.vo.SignUpRequest;
import kr.or.dining_together.member.vo.StoreProfileRequest;
import kr.or.dining_together.member.vo.StoreProfileResponse;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Before
	public void setUp() throws Exception {
		Customer user = Customer.builder()
			.email("jifrozen@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user);

		Store user1 = Store.builder()
			.email("jifrozen1@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user1);

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void signUpTest() {

		SignUpRequest signUpRequest = SignUpRequest.builder()
			.email("qja9605@naver.com")
			.name("신태범")
			.password("1234")
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

	@Test
	public void getUserId() throws Throwable {

		String email = "jifrozen@naver.com";
		Optional<User> user = userRepository.findByEmail(email);

		UserIdDto userIdDto = userService.getUserId(email);

		assertEquals(user.get().getId(), userIdDto.getId());

	}

	@Test
	public void getCustomer() throws Throwable {
		String email = "jifrozen@naver.com";
		Optional<User> user = userRepository.findByEmail(email);
		Customer userIdDto = userService.getCustomer(email);

		assertEquals(user.get().getId(), userIdDto.getId());

	}

	@Test
	public void getStore() throws Throwable {

		String email = "jifrozen1@naver.com";
		Optional<User> user = userRepository.findByEmail(email);
		Store userIdDto = userService.getStore(email);

		assertEquals(user.get().getId(), userIdDto.getId());

	}

	@Test
	public void customerModify() throws Throwable {
		Customer user = Customer.builder()
			.email("jifrozen33@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.age(2)
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user);
		String email = "jifrozen33@naver.com";

		CustomerProfileRequest customerProfileRequest =
			CustomerProfileRequest.builder()
				.age(1)
				.gender("male")
				.name("test11")
				.password(user.getPassword())
				.build();

		CustomerProfileResponse customerProfileResponse = userService.modify(customerProfileRequest, email);
		assertEquals(customerProfileResponse.getName(), "test11");

	}

	@Test
	public void storeModify() throws Throwable {
		Store user = Store.builder()
			.email("jifrozen22@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user);
		String email = "jifrozen22@naver.com";

		StoreProfileRequest storeProfileRequest =
			StoreProfileRequest.builder()
				.name("test11111")
				.password(user.getPassword())
				.build();

		StoreProfileResponse storeProfileResponse = userService.modify(storeProfileRequest, email);
		assertEquals(storeProfileResponse.getName(), "test11111");

	}
	// @Test
	// public void updatePassword() {
	//
	// 	User user = User.builder()
	// 		.id(1L)
	// 		.email("jifrozen@naver.com")
	// 		.name("문지언")
	// 		.password(passwordEncoder.encode("test1111"))
	// 		.joinDate(new Date())
	// 		.roles(Collections.singletonList("ROLE_USER"))
	// 		.build();
	// 	System.out.println(passwordEncoder.matches("test1111", user.getPassword()));
	// 	userRepository.save(user);
	//
	// }

}
