package kr.or.dining_together.member.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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

import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Menu;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.MenuRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class MenuControllerTest {

	String token;
	Store user1;
	Menu menu;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();

		Store store = Store.builder()
			.email("jifrozen1@naver.com")
			.name("문지언")
			.password(passwordEncoder.encode("test1111"))
			.joinDate(new Date())
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		user1 = storeRepository.save(store);

		String content1 = objectMapper.writeValueAsString(new LoginRequest("jifrozen1@naver.com", "test1111"));
		//when//then
		MvcResult result1 = mockMvc.perform(RestDocumentationRequestBuilders.post("/member/auth/signin")
			.content(content1)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		String resultTostring1 = result1.getResponse().getContentAsString();
		JacksonJsonParser jsonParser1 = new JacksonJsonParser();
		token = jsonParser1.parseMap(resultTostring1).get("data").toString();

		menu = Menu.builder()
			.store(user1)
			.price(1)
			.path("dd")
			.name("ddd")
			.description("ff")
			.build();

		menu = menuRepository.save(menu);
	}

	@AfterEach
	public void tearDown() throws Exception {
		menuRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void menus() throws Exception {

		Store store = storeRepository.findById(user1.getId()).orElseThrow(UserNotFoundException::new);

		Menu menu1 = Menu.builder()
			.store(store)
			.price(1)
			.path("dd")
			.name("ddd")
			.description("ff")
			.build();

		Menu menu = menuRepository.save(menu1);

		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/member/{storeId}/menus", store.getId()))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("menus",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("list.[].name").description("메뉴 이름"),
					fieldWithPath("list.[].path").description("메뉴 사진 경로"),
					fieldWithPath("list.[].description").description("메뉴 설명"),
					fieldWithPath("list.[].price").description("메뉴 가격")
				)));

	}

	@Test
	void registerMenu() throws Exception {
		MockMultipartFile file
			= new MockMultipartFile(
			"file",
			"hello.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"Hello, World!".getBytes()
		);

		mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/member/menus").file(file)
			.param("name", "dd")
			.param("price", String.valueOf(1))
			.param("description", "dddd")
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("registerMenu",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				requestParts(
					partWithName("file").description("The file to upload")
				),
				requestParameters(
					parameterWithName("name").description("이름"),
					parameterWithName("price").description("이름"),
					parameterWithName("description").description("이름")

				),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.menuId").description("Menu ID"),
					fieldWithPath("data.name").description("메뉴이름"),
					fieldWithPath("data.path").description("매뉴 사진 경로"),
					fieldWithPath("data.price").description("메뉴 가격"),
					fieldWithPath("data.description").description("메뉴 설명")
				)));
	}

	@Test
	void modifyMenu() throws Exception {
		MockMultipartFile file
			= new MockMultipartFile(
			"file",
			"hello.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"Hello, World!".getBytes()
		);
		mockMvc.perform(
			RestDocumentationRequestBuilders.fileUpload("/member/menus/{menuId}", menu.getMenuId()).file(file)
				.param("name", "ddd")
				.param("price", String.valueOf(1))
				.param("description", "dddd1")
				.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("modifyMenu",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				requestParts(
					partWithName("file").description("The file to upload")
				),
				requestParameters(
					parameterWithName("name").description("이름"),
					parameterWithName("price").description("이름"),
					parameterWithName("description").description("이름")

				),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.menuId").description("Menu ID"),
					fieldWithPath("data.name").description("메뉴이름"),
					fieldWithPath("data.path").description("매뉴 사진 경로"),
					fieldWithPath("data.price").description("메뉴 가격"),
					fieldWithPath("data.description").description("메뉴 설명")
				)));
	}

	@Test
	void deleteMenu() throws Exception {

		mockMvc.perform(RestDocumentationRequestBuilders.
			delete("/member/menus/{menuId}", menu.getMenuId())
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("deleteMenu",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data").description("성공/실패")

				)));
	}
}