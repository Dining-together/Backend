package kr.or.dining_together.member.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.service.EmailService;
import kr.or.dining_together.member.service.RedisUtil;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.SignUpRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional

public class SignControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WebApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
			.alwaysDo(print())
			.build();

		userRepository.save(Customer.builder()
			.email("jifrozen@naver.com")
			.name("?????????")
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.age(23)
			.gender("FEMALE")
			.build());
	}

	@Test
	@DisplayName("????????? ?????????")
	public void signIn() throws Exception {
		//given
		String content = objectMapper.writeValueAsString(new LoginRequest("jifrozen@naver.com", "test1111"));

		System.out.println(content);
		//when

		//when//then

		mockMvc.perform(post("/member/auth/signin")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			//then
			.andExpect(status().isOk())

			.andDo(document("login",
				requestFields(
					fieldWithPath("email").description("?????? ?????????(?????????)"),
					fieldWithPath("password").description("?????? ????????????")
				),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????"),
					fieldWithPath("data").description("?????????")
				)
			));

	}

	@Test
	@DisplayName("???????????? ?????????")
	public void signup() throws Exception {
		//given

		SignUpRequest signUpRequest = SignUpRequest.builder()
			.email("jifrozen1@naver.com")
			.name("?????????1")
			.password("test2222")
			.userType(UserType.CUSTOMER)
			.age(23)
			.gender("FEMALE")
			.build();

		Gson gson = new Gson();
		String content = gson.toJson(signUpRequest);

		System.out.println(content);
		//when//then
		mockMvc.perform(post("/member/auth/signup")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())

			.andDo(document("signup",
				requestFields(
					fieldWithPath("email").description("?????? ?????????(?????????)"),
					fieldWithPath("name").description("?????? ??????"),
					fieldWithPath("password").description("?????? ????????????"),
					fieldWithPath("userType").description("?????? ??????"),
					fieldWithPath("age").description("??????"),
					fieldWithPath("gender").description("??????")
				),
				responseFields(
					fieldWithPath("success").description("????????????"),
					fieldWithPath("code").description("????????????"),
					fieldWithPath("msg").description("?????????")
				)
			));
	}

	@Test
	@DisplayName("????????? ?????? ?????????")
	public void emailVerify() throws Exception {
		String email = "qja9605@naver.com";
		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
		info.add("email", email);

		mockMvc.perform(post("/member/auth/verify")
			.params(info)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		// .andDo(document("verifyKeyRequest",
		// 	requestFields(
		// 		fieldWithPath("email").description("?????? ?????????(?????????)")
		// 	),
		// 	responseFields(
		// 		fieldWithPath("success").description("????????????"),
		// 		fieldWithPath("code").description("????????????"),
		// 		fieldWithPath("msg").description("?????????")
		// 	)
		// ));

		emailService.sendVerificationMail(email);
		String verifyKey = redisUtil.getData(email);

		MultiValueMap<String, String> verifyInfo = new LinkedMultiValueMap<>();
		verifyInfo.add("email", email);
		verifyInfo.add("key", verifyKey);

		mockMvc.perform(get("/member/auth/verify")
			.params(verifyInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		// .andDo(document("verify",
		// 	requestFields(
		// 		fieldWithPath("email").description("?????? ?????????(?????????)"),
		// 		fieldWithPath("key").description("????????? ???")
		// 	),
		// 	responseFields(
		// 		fieldWithPath("success").description("????????????"),
		// 		fieldWithPath("code").description("????????????"),
		// 		fieldWithPath("msg").description("?????????")
		// 	)
		// ));
	}
}