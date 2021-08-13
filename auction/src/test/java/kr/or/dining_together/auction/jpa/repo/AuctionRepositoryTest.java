package kr.or.dining_together.auction.jpa.repo;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;

@RunWith(SpringRunner.class)
@DataJpaTest
class AuctionRepositoryTest {
	Auction auction1;
	@Autowired
	private AuctionRepository auctionRepository;
	LocalDateTime currentDateTime = LocalDateTime.now();
	@BeforeEach
	void setUp() {

		UserIdDto userIdDto = UserIdDto.builder()
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
			.reservation(currentDateTime)
			.deadline(currentDateTime)
			.build();

		auctionRepository.save(auction1);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void findAllByUserId() {
	}

}