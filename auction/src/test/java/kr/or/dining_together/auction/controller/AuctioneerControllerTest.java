package kr.or.dining_together.auction.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

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

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.AuctioneerRepository;
import kr.or.dining_together.auction.service.AuctioneerService;
import kr.or.dining_together.auction.vo.AuctioneerRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class AuctioneerControllerTest {
	@Autowired
	AuctionRepository auctionRepository;

	@Autowired
	AuctioneerRepository auctioneerRepository;
	@Autowired
	AuctioneerService auctioneerService;
	@Autowired
	UserServiceClient userServiceClient;
	@Autowired
	ModelMapper modelMapper;
	UserIdDto userIdDto;
	Auction auction;
	Auctioneer auctioneer;
	String token;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	LocalDateTime currentDateTime = LocalDateTime.now();
	@BeforeEach
	void setUp() {
		token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYyNDQyMDI1MSwiZXhwIjoxNjI0NTA2NjUxfQ.beMJYnqZAF1WoTvumtmZiePrU4XmaHsqjBWrWvO2B60";
		userIdDto = userServiceClient.getUserId(token);

		System.out.println(userIdDto.getId());
		Auction auction1 = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.userId(userIdDto.getId())
			.reservation(currentDateTime)
			.deadline(currentDateTime)
			.build();

		auction = auctionRepository.save(auction1);

		Auctioneer auctioneer1 = Auctioneer.builder()
			.menu("menu")
			.auction(auction)
			.content("content")
			.price(1)
			.storeId(userIdDto.getId())
			.build();

		auctioneer = auctioneerRepository.save(auctioneer1);
	}

	@Test
	void auctioneers() throws Exception {

		Auction auction1 = auctionRepository.findById(auction.getAuctionId())
			.orElseThrow(ResourceNotExistException::new);

		Auctioneer auctioneer1 = Auctioneer.builder()
			.menu("menu1")
			.auction(auction1)
			.content("content1")
			.price(1)
			.storeId(userIdDto.getId())
			.build();

		Auctioneer auctioneer = auctioneerRepository.save(auctioneer1);

		mockMvc.perform(RestDocumentationRequestBuilders.
			get("/auction/{auctionId}/auctioneers", auction1.getAuctionId()))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("auctioneers",
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("list.[].auctioneerId").description("Auctioneer ID"),
					fieldWithPath("list.[].storeId").description("참여 업체 id"),
					fieldWithPath("list.[].menu").description("추천 메뉴"),
					fieldWithPath("list.[].price").description("예상 가격"),
					fieldWithPath("list.[].content").description("내용")
				)));

	}

	@Test
	void registerAuctioneer() throws Exception {
		AuctioneerRequest auctioneerRequest = AuctioneerRequest.builder()
			.price(10)
			.menu("menu")
			.content("content")
			.build();

		String content = objectMapper.writeValueAsString(auctioneerRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.
			post("/auction/{auctionId}/auctioneer", auction.getAuctionId())
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("registerAuctioneer",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.auctioneerId").description("Auctioneer ID"),
					fieldWithPath("data.storeId").description("참여 업체 id"),
					fieldWithPath("data.menu").description("추천 메뉴"),
					fieldWithPath("data.price").description("예상 가격"),
					fieldWithPath("data.content").description("내용")
				)));
	}

	@Test
	void modifyAuctioneer() throws Exception {

		AuctioneerRequest auctioneerRequest = AuctioneerRequest.builder()
			.price(10)
			.menu("수정")
			.content("수정")
			.build();

		String content = objectMapper.writeValueAsString(auctioneerRequest);

		mockMvc.perform(RestDocumentationRequestBuilders.
			put("/auction/auctioneer/{auctioneerId}", auctioneer.getAuctioneerId())
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("modifyAuctioneer",
				requestHeaders(
					headerWithName("X-AUTH-TOKEN").description(
						"토큰값")),
				responseFields(
					fieldWithPath("success").description("성공여부"),
					fieldWithPath("code").description("코드번호"),
					fieldWithPath("msg").description("메시지"),
					fieldWithPath("data.auctioneerId").description("Auctioneer ID"),
					fieldWithPath("data.storeId").description("참여 업체 id"),
					fieldWithPath("data.menu").description("추천 메뉴"),
					fieldWithPath("data.price").description("예상 가격"),
					fieldWithPath("data.content").description("내용")
				)));
	}

	@Test
	void deleteAuctioneer() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.
			delete("/auction/auctioneer/{auctioneerId}", auctioneer.getAuctioneerId())
			.header("X-AUTH-TOKEN", token))
			.andDo(print())
			.andExpect(status().isOk())

			.andDo(document("deleteAuctioneer",
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