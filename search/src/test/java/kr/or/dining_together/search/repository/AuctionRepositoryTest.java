package kr.or.dining_together.search.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
public class AuctionRepositoryTest {

	@Autowired
	private AuctionRepository auctionRepository;

	@Before
	public void setup() {
		List<Auction> auctions = new ArrayList();

		System.out.println(LocalDateTime.now());
		for (int i = 1; i <= 10; i++) {
			String auctionId = String.valueOf(i);
			Auction auction = new Auction().builder()
				.id(auctionId)
				.reservation(LocalDateTime.now())
				.title("경매2")
				.userType("회식")
				.storeType("치킨")
				.maxPrice(10000)
				.build();
			auctions.add(auction);
		}

		auctionRepository.saveAll(auctions);
	}

	@Test
	public void findAllByTitleTest() {
		Auction auction = auctionRepository.findAllByTitle("경매2").get(0);

		assertEquals(auction.getStoreType(), "치킨");
	}

	@Test
	public void deleteById() {
		auctionRepository.deleteById("1");
		assertTrue(auctionRepository.findById("1").isEmpty());
	}

	@Test
	public void IncludedKeywordSearchTest() {
		List<Auction> auctions = auctionRepository.findAllByTitleContaining("경");

		assertEquals(auctions.size(), 10);
	}

	@Test
	public void NotIncludedKeywordSearchTest() {
		List<Auction> auctions2 = auctionRepository.findAllByTitleContaining("경매2222222");

		assertTrue(auctions2.isEmpty());
	}

}

