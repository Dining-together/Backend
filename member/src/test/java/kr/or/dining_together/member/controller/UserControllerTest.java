package kr.or.dining_together.member.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.CustomerProfileRequest;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.StoreProfileRequest;

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
@AutoConfigureRestDocs
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
	private String token1;

	@BeforeEach
	void setUp() throws Exception {
		Customer user = Customer.builder()
			.email("jifrozen@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
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

		Store user1 = Store.builder()
			.email("jifrozen1@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(user1);

		String content1 = objectMapper.writeValueAsString(new LoginRequest("jifrozen1@naver.com", "test1111"));
		//when//then
		MvcResult result1 = mockMvc.perform(post("/member/auth/signin")
			.content(content1)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String resultTostring1 = result1.getResponse().getContentAsString();
		JacksonJsonParser jsonParser1 = new JacksonJsonParser();
		token1 = jsonParser1.parseMap(resultTostring1).get("data").toString();

	}

	@AfterEach
	public void tearDown() throws Exception {
		userRepository.deleteAll();

	}

	@Test
	void getCustomer() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders
			.get("/member/customer")
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("getCustomer",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.createdDate").description("생성일자"),
					fieldWithPath("data.updatedDate").description("수정일자"),
					fieldWithPath("data.id").description("사용자 id"),
					fieldWithPath("data.email").description("사용자 이메일(아이디)"),
					fieldWithPath("data.name").description("사용자 이름"),
					fieldWithPath("data.joinDate").description("사용자 가입일자"),
					fieldWithPath("data.provider").description("회원가입 제공자"),
					fieldWithPath("data.age").description("사용자 나이"),
					fieldWithPath("data.gender").description("사용자 성별"),
					fieldWithPath("data.roles").description("사용자 타입"),
					fieldWithPath("data.authorities.[].authority").description("사용자 권한")

				)
			));
	}

	@Test
	void getStore() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders
			.get("/member/store")
			.header("X-AUTH-TOKEN", token1))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("getStore",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.createdDate").description("생성일자"),
					fieldWithPath("data.updatedDate").description("수정일자"),
					fieldWithPath("data.id").description("사용자 id"),
					fieldWithPath("data.email").description("사용자 이메일(아이디)"),
					fieldWithPath("data.name").description("사용자 이름"),
					fieldWithPath("data.joinDate").description("사용자 가입일자"),
					fieldWithPath("data.provider").description("회원가입 제공자"),
					fieldWithPath("data.roles").description("사용자 타입"),
					fieldWithPath("data.authorities.[].authority").description("사용자 권한"),
					fieldWithPath("data.documentChecked").description("서류 인증 여부"),
					fieldWithPath("data.menus").description("업체 메뉴"),
					fieldWithPath("data.phoneNum").description("업체 메뉴"),
					fieldWithPath("data.addr").description("업체 메뉴"),
					fieldWithPath("data.storeName").description("업체 메뉴")

				)
			));
	}

	@Test
	void getUserId() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders
			.get("/member/userId")
			.header("X-AUTH-TOKEN", token1))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("getUserId",
				responseFields(
					fieldWithPath("id").description("사용자 id"),
					fieldWithPath("name").description("사용자 이름")
				)
			));
	}

	@Test
	void modifyCustomer() throws Exception {

		String content = objectMapper.writeValueAsString(new CustomerProfileRequest("test1111", "test11", 1, "male"));

		mockMvc.perform(RestDocumentationRequestBuilders.
			put("/member/customer")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN", token))

			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("modifyCustomer",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				requestFields(
					fieldWithPath("password").description("유저 비밀번호"),
					fieldWithPath("name").description("사용자 이름"),
					fieldWithPath("gender").description("사용자 성별"),
					fieldWithPath("age").description("사용자 나이")
				),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.email").description("사용자 이메일(아이디)"),
					fieldWithPath("data.password").description("유저 비밀번호"),
					fieldWithPath("data.name").description("사용자 이름"),
					fieldWithPath("data.age").description("사용자 나이"),
					fieldWithPath("data.gender").description("사용자 성별")

				)
			));
	}

	@Test
	void modifyStore() throws Exception {

		String content = objectMapper.writeValueAsString(new StoreProfileRequest("test1111", "test11"));

		mockMvc.perform(RestDocumentationRequestBuilders.
			put("/member/store")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN", token1))

			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("modifyStore",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				requestFields(
					fieldWithPath("password").description("유저 비밀번호"),
					fieldWithPath("name").description("사용자 이름")
				),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.email").description("사용자 이메일(아이디)"),
					fieldWithPath("data.password").description("유저 비밀번호"),
					fieldWithPath("data.name").description("사용자 이름")
				)
			));
	}

	@Test
	void delete() throws Exception {
		Optional<User> user = userRepository.findByEmail("jifrozen@naver.com");
		assertTrue(user.isPresent());
		mockMvc.perform(RestDocumentationRequestBuilders
			.delete("/member/user")
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("delete",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지")
				)
			));
		assertTrue(userRepository.findByEmail("jifrozen@naver.com").isEmpty());
	}

}