package kr.or.dining_together.auction.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.service.AuctionService;

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
	Auction auction;
	UserIdDto userIdDto;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {

		userIdDto = UserIdDto.builder()
			.id("1")
			.name("moon")
			.build();

		auction = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.userType("Family")
			.userId("1")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auctionRepository.save(auction);
	}

	@Test
	void auctions() throws Exception {
		FieldDescriptor[] auction = new FieldDescriptor[] {
			fieldWithPath("auctionId").description("Title of the book"),
			fieldWithPath("title").description("Title of the book"),
			fieldWithPath("content").description("Author of the book"),
			fieldWithPath("maxPrice").description("Title of the book"),
			fieldWithPath("minPrice").description("Title of the book"),
			fieldWithPath("userType").description("Title of the book"),
			fieldWithPath("userId").description("Title of the book"),
			fieldWithPath("reservation").description("Title of the book"),
			fieldWithPath("userId").description("Title of the book"),
			fieldWithPath("auctioneer").description("Title of the book"),
			fieldWithPath("auctionStoreTypes").description("Title of the book"),
			fieldWithPath("createdDate").description("Title of the book"),
			fieldWithPath("updatedDate").description("Title of the book")
		};

		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/auction/auctions"))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("getAuctions",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("list.[].auctionId").description("Title of the book"),
					fieldWithPath("list.[].title").description("Title of the book"),
					fieldWithPath("list.[].content").description("Author of the book"),
					fieldWithPath("list.[].maxPrice").description("Title of the book"),
					fieldWithPath("list.[].minPrice").description("Title of the book"),
					fieldWithPath("list.[].userType").description("Title of the book"),
					fieldWithPath("list.[].userId").description("Title of the book"),
					fieldWithPath("list.[].reservation").description("Title of the book"),
					fieldWithPath("list.[].userId").description("Title of the book"),
					fieldWithPath("list.[].status").description("Title of the book"),
					fieldWithPath("list.[].deadline").description("Title of the book"),
					fieldWithPath("list.[].auctioneers").description("Title of the book"),
					fieldWithPath("list.[].auctionStoreTypes").description("Title of the book"),
					fieldWithPath("list.[].createdDate").description("Title of the book"),
					fieldWithPath("list.[].updatedDate").description("Title of the book")
				)));
	}

	@Test
	void auction() {
	}

	@Test
	void registerAuction() {
	}

	@Test
	void getAuction() {
	}

	@Test
	void modifyAuction() {
	}

	@Test
	void deleteAuction() {
	}
}