package kr.or.dining_together.auction.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.AuctioneerRepository;
import kr.or.dining_together.auction.vo.AuctioneerRequest;

/**
 * @package : kr.or.dining_together.auction.service
 * @name: AuctioneerServiceTest.java
 * @date : 2021/06/16 9:05 오후
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
class AuctioneerServiceTest {
	@Autowired
	AuctioneerRepository auctioneerRepository;
	@Autowired
	AuctionRepository auctionRepository;
	@Autowired
	AuctioneerService auctioneerService;

	Auction auction;

	Auctioneer auctioneer;

	UserIdDto userIdDto;

	@BeforeEach
	void setUp() {
		userIdDto = UserIdDto.builder()
			.id(1L)
			.name("moon")
			.build();

		Auction auction1 = Auction.builder()
			.content("내용")
			.title("공고임")
			.deadline(new Date())
			.maxPrice(10)
			.minPrice(0)
			.userId(1L)
			.userType("회식")
			.reservation(new Date())
			.build();

		auction = auctionRepository.save(auction1);

		Auctioneer auctioneer1 = Auctioneer.builder()
			.content("우리 업체는~")
			.auction(auction)
			.price(10000)
			.menu("메뉴")
			.storeId(1L)
			.build();

		auctioneer = auctioneerRepository.save(auctioneer1);
	}

	@Test
	void getAuctioneer() {
		List<Auctioneer> auctioneers = auctioneerService.getAuctioneers(auction.getAuctionId());
		assertFalse(auctioneers.isEmpty());
	}

	@Test
	void registerAuctioneer() {
		AuctioneerRequest auctioneerRequest = AuctioneerRequest.builder()
			.content("content")
			.menu("menu")
			.price(10)
			.build();

		Auctioneer auctioneer = auctioneerService.registerAuctioneer(auctioneerRequest, userIdDto,
			auction.getAuctionId());

		assertEquals(auctioneer.getContent(), "content");

	}

	@Test
	void modifyAuctioneer() {
		AuctioneerRequest auctioneerRequest = AuctioneerRequest.builder()
			.content("content1")
			.menu("menu1")
			.price(101)
			.build();

		Auctioneer auctioneer1 = auctioneerService.modifyAuctioneer(auctioneerRequest, auctioneer.getAuctioneerId());

		assertEquals(auctioneer1.getContent(), "content1");

	}

	@Test
	void deleteAuctioneer() {
		auctioneerService.deleteAuctioneer(auctioneer.getAuctioneerId());
		assertTrue(auctioneerRepository.findById(auctioneer.getAuctioneerId()).isEmpty());
	}
}