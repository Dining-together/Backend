package kr.or.dining_together.member.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: UserControllerTest.java
 * @date : 2021/05/30 11:25 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : UserController Test
 * @modified :
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String token;

	@BeforeEach
	void setUp() throws Exception {
		User user = User.builder()
			.id(1L)
			.email("jifrozen@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.phoneNo("010-1234-5678")
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user);

		String content = objectMapper.writeValueAsString(new LoginRequest("jifrozen@naver.com", "test1111"));
		//when//then
		MvcResult result = mockMvc.perform(post("/member/auth/signin")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String resultTostring = result.getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		token = jsonParser.parseMap(resultTostring).get("data").toString();

	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	void findUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
			.get("/member/user")
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void verifyPassword() throws Exception{
		Optional<User> user = userRepository.findByEmail("jifrozen@naver.com");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("password", "test1111");

		mockMvc.perform(MockMvcRequestBuilders
			.put("/member/password/verification")
			.header("X-AUTH-TOKEN", token)
			.params(params))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	void changePassword() throws Exception {
		Optional<User> user = userRepository.findByEmail("jifrozen@naver.com");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("newPassword", "test111");

		mockMvc.perform(MockMvcRequestBuilders
			.put("/member/password")
			.header("X-AUTH-TOKEN", token)
			.params(params))
			.andDo(print())
			.andExpect(status().isOk());

		assertTrue(passwordEncoder.matches("test111",user.get().getPassword()));
	}

	@Test
	void delete() throws Exception {
		Optional<User> user = userRepository.findByEmail("jifrozen@naver.com");
		assertTrue(user.isPresent());
		mockMvc.perform(MockMvcRequestBuilders
			.delete("/member/user")
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk());
		assertTrue(userRepository.findByEmail("jifrozen@naver.com").isEmpty());
	}

}