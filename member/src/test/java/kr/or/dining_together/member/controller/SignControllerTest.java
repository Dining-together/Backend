package kr.or.dining_together.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.SignUpRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SignControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Before
	public void setUp() throws Exception {
		userRepository.save(Customer.builder()
			.email("jifrozen@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.age(23)
			.gender("FEMALE")
			.build());
	}

	@Test
	public void signIn() throws Exception {
		//given
		String content = objectMapper.writeValueAsString(new LoginRequest("jifrozen@naver.com", "test1111"));
		System.out.println(content);
		//when
		mockMvc.perform(post("/member/auth/signin")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			//then
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void signup() throws Exception {
		//given
		UserDto userDto = UserDto.builder()
			.email("jifrozen1@naver.com")
			.name("문지언1")
			.password("test2222")
			.roles(Collections.singletonList("ROLE_USER"))
			.build();
		SignUpRequest signUpRequest = SignUpRequest.builder()
			.userDto(userDto)
			.userType(UserType.CUSTOMER)
			.age(23)
			.gender("FEMALE")
			.build();

		Gson gson = new Gson();
		String content = gson.toJson(userDto);

		System.out.println(content);
		//when//then
		mockMvc.perform(post("/member/auth/signup")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

}