package kr.or.dining_together.member.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: UserControllerTest.java
 * @date : 2021/05/30 11:25 ??????
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

	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
			.alwaysDo(print())
			.build();

		Customer user = Customer.builder()
			.email("jifrozen@naver.com")
			.name("?????????")
			.password(passwordEncoder.encode("test1111"))
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
			.name("?????????")
			.password(passwordEncoder.encode("test1111"))
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
						"?????????")),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????"),
					fieldWithPath("data.createdDate").description("????????????"),
					fieldWithPath("data.updatedDate").description("????????????"),
					fieldWithPath("data.id").description("????????? id"),
					fieldWithPath("data.email").description("????????? ?????????(?????????)"),
					fieldWithPath("data.name").description("????????? ??????"),
					fieldWithPath("data.joinDate").description("????????? ????????????"),
					fieldWithPath("data.provider").description("???????????? ?????????"),
					fieldWithPath("data.age").description("????????? ??????"),
					fieldWithPath("data.gender").description("????????? ??????"),
					fieldWithPath("data.path").description("????????? ??????")

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
						"?????????")),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????"),
					fieldWithPath("data.createdDate").description("????????????"),
					fieldWithPath("data.updatedDate").description("????????????"),
					fieldWithPath("data.id").description("????????? id"),
					fieldWithPath("data.email").description("????????? ?????????(?????????)"),
					fieldWithPath("data.name").description("????????? ??????"),
					fieldWithPath("data.joinDate").description("????????? ????????????"),
					fieldWithPath("data.provider").description("???????????? ?????????"),
					fieldWithPath("data.documentChecked").description("?????? ?????? ??????"),
					fieldWithPath("data.path").description("????????? ??????"),
					fieldWithPath("data.phoneNum").description("?????? ??????"),
					fieldWithPath("data.addr").description("?????? ??????"),
					fieldWithPath("data.storeImages").description("?????? ??????"),
					fieldWithPath("data.facility").description("?????? ??????"),
					fieldWithPath("data.storeType").description("?????? ??????"),
					fieldWithPath("data.storeName").description("?????? ??????")

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
					fieldWithPath("id").description("????????? id"),
					fieldWithPath("type").description("????????? ??????"),
					fieldWithPath("name").description("????????? ??????")
				)
			));
	}

	@Test
	void modifyCustomer() throws Exception {

		// String content = objectMapper.writeValueAsString(new CustomerProfileRequest("test1111", "test11", 1, "male"));
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.
		// 	put("/member/customer")
		// 	.content(content)
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.accept(MediaType.APPLICATION_JSON)
		// 	.header("X-AUTH-TOKEN", token))
		//
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("modifyCustomer",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"?????????")),
		// 		requestFields(
		// 			fieldWithPath("password").description("?????? ????????????"),
		// 			fieldWithPath("name").description("????????? ??????"),
		// 			fieldWithPath("gender").description("????????? ??????"),
		// 			fieldWithPath("age").description("????????? ??????")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("????????????"),
		// 			fieldWithPath("code").description("????????????"),
		// 			fieldWithPath("msg").description("?????????"),
		// 			fieldWithPath("data.email").description("????????? ?????????(?????????)"),
		// 			fieldWithPath("data.password").description("?????? ????????????"),
		// 			fieldWithPath("data.name").description("????????? ??????"),
		// 			fieldWithPath("data.age").description("????????? ??????"),
		// 			fieldWithPath("data.gender").description("????????? ??????")
		//
		// 		)
		// 	));
	}

	@Test
	void modifyStore() throws Exception {

		// String content = objectMapper.writeValueAsString(new StoreProfileRequest("test1111", "test11"));
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.
		// 	put("/member/store")
		// 	.content(content)
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.accept(MediaType.APPLICATION_JSON)
		// 	.header("X-AUTH-TOKEN", token1))
		//
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("modifyStore",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"?????????")),
		// 		requestFields(
		// 			fieldWithPath("password").description("?????? ????????????"),
		// 			fieldWithPath("name").description("????????? ??????")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("????????????"),
		// 			fieldWithPath("code").description("????????????"),
		// 			fieldWithPath("msg").description("?????????"),
		// 			fieldWithPath("data.email").description("????????? ?????????(?????????)"),
		// 			fieldWithPath("data.password").description("?????? ????????????"),
		// 			fieldWithPath("data.name").description("????????? ??????")
		// 		)
		// 	));
	}

	@Test
	void saveFile() throws Exception {
		MockMultipartFile file
			= new MockMultipartFile(
			"file",
			"hello.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"Hello, World!".getBytes()
		);
		mockMvc.perform(
			RestDocumentationRequestBuilders.fileUpload("/member/image").file(file)
				.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("saveFile",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"?????????")),
				requestParts(
					partWithName("file").description("The file to upload")
				),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????")
				)));
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
						"?????????")),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????")
				)
			));
		assertTrue(userRepository.findByEmail("jifrozen@naver.com").isEmpty());
	}

}