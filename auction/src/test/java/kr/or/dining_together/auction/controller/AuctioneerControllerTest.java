package kr.or.dining_together.auction.controller;

import static org.junit.jupiter.api.Assertions.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.AuctioneerRepository;
import kr.or.dining_together.auction.service.AuctionService;
import kr.or.dining_together.auction.service.AuctioneerService;

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
	@Autowired
	private ObjectMapper objectMapper;
	UserIdDto userIdDto;
	@Autowired
	private MockMvc mockMvc;

	Auction auction;
	Auctioneer auctioneer;
	@BeforeEach
	void setUp() {
		userIdDto = UserIdDto.builder()
			.id(1L)
			.name("moon")
			.build();

		System.out.println(userIdDto.getId());
		Auction auction1 = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.userType("Family")
			.userId(1L)
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auction=auctionRepository.save(auction1);

		Auctioneer auctioneer1=Auctioneer.builder()
			.menu("menu")
			.auction(auction)
			.content("content")
			.price(1)
			.storeId(1L)
			.build();

		auctioneer=auctioneerRepository.save(auctioneer1);
	}

	@Test
	void auctioneers() {

	}

	@Test
	void registerAuctioneer() {
	}

	@Test
	void modifyAuction() {
	}

	@Test
	void deleteAuction() {
	}
}