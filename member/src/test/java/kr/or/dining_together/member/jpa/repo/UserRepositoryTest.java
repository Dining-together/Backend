package kr.or.dining_together.member.jpa.repo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.vo.LoginRequest;
import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class UserRepositoryTest {
	@Autowired
	UserRepository userRepository;

	@Test
	public void insertTest() {
		List<String> roles = Arrays.asList("USER");
		User user = User.builder()
			.id(1L)
			.email("qja9605@naver.com")
			.name("신태범")
			.password("1234")
			.phoneNo("010-2691-3895")
			.joinDate(new Date())
			.roles(roles)
			.build();

		userRepository.save(user);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("qja9605@naver.com");
		loginRequest.setPassword("1234");

		System.out.println(user);

	}

}