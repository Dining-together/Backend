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

import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuctionServiceTest {

	@Autowired
	AuctionRepository auctionRepository;

	@Autowired
	AuctionService auctionService;

	@Autowired
	ModelMapper modelMapper;

	Auction auction;

	@BeforeEach
	void setUp() {
		auction = Auction.builder()
			.auctionId(1L)
			.title("제목")
			.content("내용")
			.maxPrice("1000")
			.minPrice("10")
			.userType("Family")
			.userId("1")
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
		long auctionId = 1L;
		Auction auction2 = auctionService.getAuction(auctionId);

		assertEquals(auction.getAuctionId(), auction2.getAuctionId());
	}

	@Test
	void getAuctionsByUserId(){
		String userId="1";
		List<Auction> auctions=auctionService.getAuctionsByUserId(userId);

		assertEquals(auctions.get(0).getUserId(),userId);

	}

	@Test
	void writeAuction() {

		long auctionId = 2L;
		Auction auction = Auction.builder()
			.auctionId(2L)
			.title("제목2")
			.content("내용2")
			.maxPrice("2000")
			.minPrice("20")
			.userType("Friend")
			.userId("2")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);


		assertEquals(auctionRepository.findById(auctionId).get().getAuctionId(), auction.getAuctionId());

	}

	@Test
	void updateAuction() {
		long auctionId = 1L;

		Auction auction = Auction.builder()
			.auctionId(1L)
			.title("제목1")
			.content("내용2")
			.maxPrice("2000")
			.minPrice("20")
			.userType("Friend")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);

		auctionService.updateAuction(auction.getAuctionId(), auctionDto);

		assertEquals(auctionRepository.findById(auctionId).get().getTitle(), "제목1");

	}

	@Test
	void deleteAuction() {
		long auctionId = 3L;
		Auction auction = Auction.builder()
			.auctionId(3L)
			.title("제목2")
			.content("내용2")
			.maxPrice("2000")
			.minPrice("20")
			.userType("Friend")
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auctionRepository.save(auction);

		auctionService.deleteAuction(auction.getAuctionId());

		assertTrue(auctionRepository.findById(auction.getAuctionId()).isEmpty());
	}

}