package kr.or.dining_together.auction.jpa.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
class AuctionRepositoryTest {
	@Autowired
	private AuctionRepository auctionRepository;
	Auction auction1;

	@BeforeEach
	void setUp() {

		UserIdDto userIdDto= UserIdDto.builder()
			.name("dd")
			.id(4)
			.type("CUSTOMER")
			.build();

		auction1 = Auction.builder()
			.title("제목")
			.content("내용")
			.maxPrice(1000)
			.minPrice(10)
			.groupType("Family")
			.userId(userIdDto.getId())
			.reservation(new Date())
			.deadline(new Date())
			.build();

		auctionRepository.save(auction1);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void findAllByUserId() {
	}

	@Test
	void updateAuctionDeadlineEnd() {
		auctionRepository.updateAuctionDeadlineEnd(new Date());
		assertEquals(false,auction1.isDeadline());

	}
}