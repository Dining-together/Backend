package kr.or.dining_together.auction.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.vo.AuctionRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuctionServiceTest {

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

	@BeforeEach
	void setUp() {

		userIdDto = UserIdDto.builder()
			.id(1L)
			.name("moon")
			.build();

		auction = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.groupType("Family")
			.userId(1L)
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auctionRepository.save(auction);

	}

	@Test
	void getAuctions() {
		List<Auction> auctions = auctionService.getAuctions();

		assertFalse(auctions.isEmpty());

	}

	@Test
	void getAuction() {
		long auctionId = auction.getAuctionId();
		Auction auction2 = auctionService.getAuction(auctionId);

		assertEquals(auction.getAuctionId(), auction2.getAuctionId());
	}

	@Test
	void getAuctionsByUserId() {
		long userId = 1L;
		List<Auction> auctions = auctionService.getAuctionsByUserId(userId);

		assertEquals(auctions.get(0).getUserId(), userId);

	}

	@Test
	void writeAuction() {
		AuctionRequest auction1 = AuctionRequest.builder()
			.title("제목2")
			.content("내용2")
			.maxPrice(2000)
			.minPrice(20)
			.groupType("Friend")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		Auction auction = auctionService.writeAuction(userIdDto, auction1);

		assertEquals(auction.getTitle(), "제목2");

	}

	@Test
	void updateAuction() {

		Auction modifyAuction = Auction.builder()
			.title("제목1")
			.content("내용2")
			.maxPrice(2000)
			.minPrice(20)
			.groupType("Friend")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		AuctionRequest auctionRequest = modelMapper.map(modifyAuction, AuctionRequest.class);

		Auction auction1 = auctionService.updateAuction(auction.getAuctionId(), auctionRequest);

		assertEquals(auction1.getTitle(), "제목1");

	}

	@Test
	void deleteAuction() {
		auctionService.deleteAuction(auction.getAuctionId());

		assertTrue(auctionRepository.findById(auction.getAuctionId()).isEmpty());
	}

}