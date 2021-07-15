package kr.or.dining_together.search.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.search.document.Auction;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionServiceTest {

	// @Autowired
	// private AuctionService auctionService;
	//
	// @Before
	// public void setup() {
	// 	List<Auction> auctions = new ArrayList();
	//
	// 	for (int i = 1; i <= 10; i++) {
	// 		String auctionId = String.valueOf(i);
	// 		Auction auction = new Auction().builder()
	// 			.id(auctionId)
	// 			.title("경매2")
	// 			.groupType("회식")
	// 			.registerDate(LocalDateTime.now())
	// 			.preferredLocation("서울대입구")
	// 			.preferredMenu("치킨")
	// 			.preferredPrice(10000)
	// 			.build();
	// 		auctions.add(auction);
	// 	}
	//
	// 	auctionService.createAuctionIndexBulk(auctions);
	// }
	//
	// @Test
	// public void createOneIndexAndDelete() throws IOException {
	// 	Auction auction = new Auction().builder()
	// 		.id("11")
	// 		.title("배고파")
	// 		.groupType("회식")
	// 		.registerDate(LocalDateTime.now())
	// 		.preferredLocation("서울대입구")
	// 		.preferredMenu("치킨")
	// 		.preferredPrice(10000)
	// 		.build();
	//
	// 	auctionService.createAuctionIndex(auction);
	// 	assertFalse(auctionService.findByTitle("배고파").isEmpty());
	//
	// 	auctionService.deleteAuctionDocument("11");
	// 	assertTrue(auctionService.findByTitle("배고파").isEmpty());
	//
	// }
	//
	// @Test
	// public void findAllByTitle() {
	// 	Auction auction = auctionService.findByTitle("경매2").get(0);
	//
	// 	System.out.println(auction);
	// }
	//
	// @Test
	// public void IncludedKeywordSearchTest() {
	// 	List<Auction> auctions = auctionService.findByTitleMatchingNames("경");
	//
	// 	assertEquals(auctions.size(), 10);
	// }
	//
	// @Test
	// public void NotIncludedKeywordSearchTest() {
	// 	List<Auction> auctions2 = auctionService.findByTitleMatchingNames("경매2222222");
	//
	// 	assertTrue(auctions2.isEmpty());
	// }
}