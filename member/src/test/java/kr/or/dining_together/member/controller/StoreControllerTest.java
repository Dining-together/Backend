package kr.or.dining_together.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;

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

import kr.or.dining_together.member.jpa.entity.FacilityEtc;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.FacilityEtcRepository;
import kr.or.dining_together.member.jpa.repo.FacilityRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class StoreControllerTest {

	String token;
	Store user1;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private FacilityRepository facilityRepository;
	@Autowired
	private FacilityEtcRepository facilityEtcRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
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

		FacilityEtc facilityEtc = FacilityEtc.builder()
			.name("aa")
			.id(1L)
			.build();

		facilityEtcRepository.save(facilityEtc);

	}

	@AfterEach
	void tearDown() {
		storeRepository.deleteAll();
	}

	@Test
	void saveFiles() throws Exception {
		// MockMultipartFile file1
		// 	= new MockMultipartFile(
		// 	"file",
		// 	"hello.jpg",
		// 	MediaType.IMAGE_JPEG_VALUE,
		// 	"Hello, World!".getBytes()
		// );
		// MockMultipartFile file2
		// 	= new MockMultipartFile(
		// 	"file",
		// 	"hello.jpg",
		// 	MediaType.IMAGE_JPEG_VALUE,
		// 	"Hello, World!".getBytes()
		// );
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/member/store/images").file(file1).file(file2))
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("saveFiles",
		// 		requestParts(
		// 			partWithName("files").description("The file to upload")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("성공여부"),
		// 			fieldWithPath("code").description("코드번호"),
		// 			fieldWithPath("msg").description("메시지")
		// 		)));

	}

	@Test
	void saveDocument() throws Exception {
		// MockMultipartFile file
		// 	= new MockMultipartFile(
		// 	"file",
		// 	"hello.jpg",
		// 	MediaType.IMAGE_JPEG_VALUE,
		// 	"Hello, World!".getBytes()
		// );
		// mockMvc.perform(
		// 	RestDocumentationRequestBuilders.fileUpload("/member/store/document").file(file)
		// 		.header("X-AUTH-TOKEN", token))
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("saveDocument",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"토큰값")),
		// 		requestParts(
		// 			partWithName("file").description("The file to upload")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("성공여부"),
		// 			fieldWithPath("code").description("코드번호"),
		// 			fieldWithPath("msg").description("메시지")
		// 		)));
	}

	@Test
	void registerStore() throws Exception {
		//
		// StoreRequest storeRequest = StoreRequest.builder()
		// 	.addr("ㅇ")
		// 	.storeName("이름")
		// 	.phoneNum("번호")
		// 	.build();
		//
		// String content = objectMapper.writeValueAsString(storeRequest);
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.
		// 	post("/member/store")
		// 	.content(content)
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.accept(MediaType.APPLICATION_JSON)
		// 	.header("X-AUTH-TOKEN", token))
		//
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("registerStore",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"토큰값")),
		// 		requestFields(
		// 			fieldWithPath("addr").description("유저 비밀번호"),
		// 			fieldWithPath("storeName").description("사용자 이름"),
		// 			fieldWithPath("phoneNum").description("사용자 성별")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("성공여부"),
		// 			fieldWithPath("code").description("코드번호"),
		// 			fieldWithPath("msg").description("메시지"),
		// 			fieldWithPath("data.createdDate").description("생성일자"),
		// 			fieldWithPath("data.updatedDate").description("수정일자"),
		// 			fieldWithPath("data.id").description("사용자 id"),
		// 			fieldWithPath("data.email").description("사용자 이메일(아이디)"),
		// 			fieldWithPath("data.name").description("사용자 이름"),
		// 			fieldWithPath("data.joinDate").description("사용자 가입일자"),
		// 			fieldWithPath("data.provider").description("회원가입 제공자"),
		// 			fieldWithPath("data.roles").description("사용자 타입"),
		// 			fieldWithPath("data.authorities.[].authority").description("사용자 권한"),
		// 			fieldWithPath("data.documentChecked").description("서류 인증 여부"),
		// 			fieldWithPath("data.path").description("사용자 사진"),
		// 			fieldWithPath("data.menus").description("업체 메뉴"),
		// 			fieldWithPath("data.phoneNum").description("업체 메뉴"),
		// 			fieldWithPath("data.addr").description("업체 메뉴"),
		// 			fieldWithPath("data.storeName").description("업체 메뉴")
		//
		// 		)
		// 	));
	}

	@Test
	void registerFacility() throws Exception {
		// List<String> FacilityEtcNames = new ArrayList<>();
		// FacilityEtcNames.add("aa");
		//
		// FacilityRequest storeRequest = FacilityRequest.builder()
		// 	.capacity(2)
		// 	.closedTime(new Date())
		// 	.openTime(new Date())
		// 	.parkingCount(3)
		// 	.parking(true)
		// 	.FacilityEtcNames(FacilityEtcNames)
		// 	.build();
		//
		// String content = objectMapper.writeValueAsString(storeRequest);
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.
		// 	post("/member/store/facility")
		// 	.content(content)
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.accept(MediaType.APPLICATION_JSON)
		// 	.header("X-AUTH-TOKEN", token))
		//
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("registerStore",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"토큰값")),
		// 		requestFields(
		// 			fieldWithPath("addr").description("유저 비밀번호"),
		// 			fieldWithPath("storeName").description("사용자 이름"),
		// 			fieldWithPath("phoneNum").description("사용자 성별")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("성공여부"),
		// 			fieldWithPath("code").description("코드번호"),
		// 			fieldWithPath("msg").description("메시지"),
		// 			fieldWithPath("data.createdDate").description("생성일자"),
		// 			fieldWithPath("data.updatedDate").description("수정일자"),
		// 			fieldWithPath("data.id").description("사용자 id"),
		// 			fieldWithPath("data.email").description("사용자 이메일(아이디)"),
		// 			fieldWithPath("data.name").description("사용자 이름"),
		// 			fieldWithPath("data.joinDate").description("사용자 가입일자"),
		// 			fieldWithPath("data.provider").description("회원가입 제공자"),
		// 			fieldWithPath("data.roles").description("사용자 타입"),
		// 			fieldWithPath("data.authorities.[].authority").description("사용자 권한"),
		// 			fieldWithPath("data.documentChecked").description("서류 인증 여부"),
		// 			fieldWithPath("data.path").description("사용자 사진"),
		// 			fieldWithPath("data.menus").description("업체 메뉴"),
		// 			fieldWithPath("data.phoneNum").description("업체 메뉴"),
		// 			fieldWithPath("data.addr").description("업체 메뉴"),
		// 			fieldWithPath("data.storeName").description("업체 메뉴")
		//
		// 		)
		// 	));
	}

	@Test
	void modifyFacility() throws Exception {
		//
		// List<String> FacilityEtcNames = new ArrayList<>();
		// FacilityEtcNames.add("aa");
		//
		// FacilityRequest storeRequest = FacilityRequest.builder()
		// 	.capacity(2)
		// 	.closedTime(new Date())
		// 	.openTime(new Date())
		// 	.parkingCount(3)
		// 	.parking(true)
		// 	.FacilityEtcNames(FacilityEtcNames)
		// 	.build();
		//
		// String content = objectMapper.writeValueAsString(storeRequest);
		//
		// mockMvc.perform(RestDocumentationRequestBuilders.
		// 	put("/member/store/facility{facilityId}")
		// 	.content(content)
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.accept(MediaType.APPLICATION_JSON)
		// 	.header("X-AUTH-TOKEN", token))
		//
		// 	.andDo(print())
		// 	.andExpect(status().isOk())
		//
		// 	.andDo(document("registerStore",
		// 		requestHeaders(
		// 			headerWithName("X-AUTH-TOKEN").description(
		// 				"토큰값")),
		// 		requestFields(
		// 			fieldWithPath("addr").description("유저 비밀번호"),
		// 			fieldWithPath("storeName").description("사용자 이름"),
		// 			fieldWithPath("phoneNum").description("사용자 성별")
		// 		),
		// 		responseFields(
		// 			fieldWithPath("success").description("성공여부"),
		// 			fieldWithPath("code").description("코드번호"),
		// 			fieldWithPath("msg").description("메시지"),
		// 			fieldWithPath("data.createdDate").description("생성일자"),
		// 			fieldWithPath("data.updatedDate").description("수정일자"),
		// 			fieldWithPath("data.id").description("사용자 id"),
		// 			fieldWithPath("data.email").description("사용자 이메일(아이디)"),
		// 			fieldWithPath("data.name").description("사용자 이름"),
		// 			fieldWithPath("data.joinDate").description("사용자 가입일자"),
		// 			fieldWithPath("data.provider").description("회원가입 제공자"),
		// 			fieldWithPath("data.roles").description("사용자 타입"),
		// 			fieldWithPath("data.authorities.[].authority").description("사용자 권한"),
		// 			fieldWithPath("data.documentChecked").description("서류 인증 여부"),
		// 			fieldWithPath("data.path").description("사용자 사진"),
		// 			fieldWithPath("data.menus").description("업체 메뉴"),
		// 			fieldWithPath("data.phoneNum").description("업체 메뉴"),
		// 			fieldWithPath("data.addr").description("업체 메뉴"),
		// 			fieldWithPath("data.storeName").description("업체 메뉴")
		//
		// 		)
		// 	));
	}
}