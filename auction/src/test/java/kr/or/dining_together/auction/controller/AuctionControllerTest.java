package kr.or.dining_together.auction.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.service.AuctionService;
import kr.or.dining_together.auction.vo.RequestAuction;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class AuctionControllerTest {

	@Autowired
	AuctionRepository auctionRepository;
	@Autowired
	AuctionService auctionService;
	@Autowired
	UserServiceClient userServiceClient;
	@Autowired
	ModelMapper modelMapper;
	UserIdDto userIdDto;
	Auction auction;
	long auctionId;
	String token;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYyNDQyMDI1MSwiZXhwIjoxNjI0NTA2NjUxfQ.beMJYnqZAF1WoTvumtmZiePrU4XmaHsqjBWrWvO2B60";
		userIdDto = userServiceClient.getUserId(token);
		// SignUpRequest sign=SignUpRequest.builder()
		// 	.email("test@test.com")
		// 	.name("test1")
		// 	.password("test21")
		// 	.userType(UserType.CUSTOMER)
		// 	.age(11)
		// 	.gender("female")
		// 	.build();
		//
		// userServiceClient.userSignUp(sign);
		//
		// LoginRequest loginRequest=new LoginRequest("test@test.com","test21");
		//
		// token=userServiceClient.login(loginRequest);

		System.out.println(userIdDto.getId());
		Auction auction1 = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.userType("Family")
			.userId(userIdDto.getId())
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auction = auctionRepository.save(auction1);
		auctionId = auction.getAuctionId();
	}

	@AfterEach
	void tearDown() {

	}

	@Test
	void auctions() throws Exception {

		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/auction/auctions"))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("auctions",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("list.[].auctionId").description("공고 ID"),
					fieldWithPath("list.[].title").description("공고 제목"),
					fieldWithPath("list.[].content").description("공고 내용"),
					fieldWithPath("list.[].maxPrice").description("공고 최대가"),
					fieldWithPath("list.[].minPrice").description("공고 최소가"),
					fieldWithPath("list.[].userType").description("공고 사용자 유형(회식, 가족, 친구)"),
					fieldWithPath("list.[].userId").description("공고 작성자"),
					fieldWithPath("list.[].reservation").description("공고 예약 시간"),
					fieldWithPath("list.[].status").description("공고 상태(진행중, 마감, 실패)"),
					fieldWithPath("list.[].deadline").description("공고 마감시간"),
					fieldWithPath("list.[].auctioneers").description("공고 경매 참가업체"),
					fieldWithPath("list.[].auctionStoreTypes").description("공고 선호 업체"),
					fieldWithPath("list.[].createdDate").description("공고 생성 일자"),
					fieldWithPath("list.[].updatedDate").description("공고 수정 일자")
				)));
	}

	@Test
	void deleteAuction() throws Exception {
		System.out.println(auction.getAuctionId());
		mockMvc.perform(RestDocumentationRequestBuilders.
			delete("/auction/{auctionId}", auction.getAuctionId())
			.header("X-AUTH-TOKEN",
				token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("deleteAuction",
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

	@Test
	void auction() throws Exception {
		System.out.println(auction.getAuctionId());
		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/auction/{auctionId}", auction.getAuctionId()))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("auction",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.auctionId").description("공고 ID"),
					fieldWithPath("data.title").description("공고 제목"),
					fieldWithPath("data.content").description("공고 내용"),
					fieldWithPath("data.maxPrice").description("공고 최대가"),
					fieldWithPath("data.minPrice").description("공고 최소가"),
					fieldWithPath("data.userType").description("공고 사용자 유형(회식, 가족, 친구)"),
					fieldWithPath("data.userId").description("공고 작성자"),
					fieldWithPath("data.reservation").description("공고 예약 시간"),
					fieldWithPath("data.status").description("공고 상태(진행중, 마감, 실패)"),
					fieldWithPath("data.deadline").description("공고 마감시간"),
					fieldWithPath("data.auctioneers").description("공고 경매 참가업체"),
					fieldWithPath("data.auctionStoreTypes").description("공고 선호 업체"),
					fieldWithPath("data.createdDate").description("공고 생성 일자"),
					fieldWithPath("data.updatedDate").description("공고 수정 일자")
				)));
	}

	@Test
	void registerAuction() throws Exception {

		RequestAuction auction1 = RequestAuction.builder()
			.title("제목1")
			.content("내용1")
			.maxPrice(1000)
			.minPrice(10)
			.userType("Family")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		String content = objectMapper.writeValueAsString(auction1);

		mockMvc.perform(RestDocumentationRequestBuilders.
			post("/auction")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN",
				token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("registerAuction",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.auctionId").description("공고 ID"),
					fieldWithPath("data.title").description("공고 제목"),
					fieldWithPath("data.content").description("공고 내용"),
					fieldWithPath("data.maxPrice").description("공고 최대가"),
					fieldWithPath("data.minPrice").description("공고 최소가"),
					fieldWithPath("data.userType").description("공고 사용자 유형(회식, 가족, 친구)"),
					fieldWithPath("data.userId").description("공고 작성자"),
					fieldWithPath("data.reservation").description("공고 예약 시간"),
					fieldWithPath("data.status").description("공고 상태(진행중, 마감, 실패)"),
					fieldWithPath("data.deadline").description("공고 마감시간"),
					fieldWithPath("data.auctioneers").description("공고 경매 참가업체"),
					fieldWithPath("data.auctionStoreTypes").description("공고 선호 업체"),
					fieldWithPath("data.createdDate").description("공고 생성 일자"),
					fieldWithPath("data.updatedDate").description("공고 수정 일자")
				)));
	}

	@Test
	void getAuction() throws Exception {
		System.out.println(auction.getAuctionId());
		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/auction/{userId}/auctions", "1"))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("getAuction",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("list.[].auctionId").description("공고 ID"),
					fieldWithPath("list.[].title").description("공고 제목"),
					fieldWithPath("list.[].content").description("공고 내용"),
					fieldWithPath("list.[].maxPrice").description("공고 최대가"),
					fieldWithPath("list.[].minPrice").description("공고 최소가"),
					fieldWithPath("list.[].userType").description("공고 사용자 유형(회식, 가족, 친구)"),
					fieldWithPath("list.[].userId").description("공고 작성자"),
					fieldWithPath("list.[].reservation").description("공고 예약 시간"),
					fieldWithPath("list.[].status").description("공고 상태(진행중, 마감, 실패)"),
					fieldWithPath("list.[].deadline").description("공고 마감시간"),
					fieldWithPath("list.[].auctioneers").description("공고 경매 참가업체"),
					fieldWithPath("list.[].auctionStoreTypes").description("공고 선호 업체"),
					fieldWithPath("list.[].createdDate").description("공고 생성 일자"),
					fieldWithPath("list.[].updatedDate").description("공고 수정 일자")
				)));
	}

	@Test
	void modifyAuction() throws Exception {
		System.out.println(auction.getAuctionId());

		AuctionDto auction1 = modelMapper.map(auction, AuctionDto.class);

		auction1.setContent("수정");

		String content = objectMapper.writeValueAsString(auction1);

		mockMvc.perform(RestDocumentationRequestBuilders.
			put("/auction/{auctionId}", auction.getAuctionId())
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN",
				token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("modifyAuction",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.auctionId").description("공고 ID"),
					fieldWithPath("data.title").description("공고 제목"),
					fieldWithPath("data.content").description("공고 내용"),
					fieldWithPath("data.maxPrice").description("공고 최대가"),
					fieldWithPath("data.minPrice").description("공고 최소가"),
					fieldWithPath("data.userType").description("공고 사용자 유형(회식, 가족, 친구)"),
					fieldWithPath("data.userId").description("공고 작성자"),
					fieldWithPath("data.reservation").description("공고 예약 시간"),
					fieldWithPath("data.status").description("공고 상태(진행중, 마감, 실패)"),
					fieldWithPath("data.deadline").description("공고 마감시간"),
					fieldWithPath("data.auctioneers").description("공고 경매 참가업체"),
					fieldWithPath("data.auctionStoreTypes").description("공고 선호 업체"),
					fieldWithPath("data.createdDate").description("공고 생성 일자"),
					fieldWithPath("data.updatedDate").description("공고 수정 일자")
				)));

	}

}