package kr.or.dining_together.search.repository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionRepositoryTest {

	// @Autowired
	// private AuctionRepository auctionRepository;
	//
	// @Before
	// public void setup() {
	// 	List<Auction> auctions = new ArrayList();
	//
	// 	System.out.println(LocalDateTime.now());
	// 	for (int i = 1; i <= 10; i++) {
	// 		String auctionId = String.valueOf(i);
	// 		Auction auction = new Auction().builder()
	// 			.id(auctionId)
	// 			.registerDate(LocalDateTime.now())
	// 			.title("경매2")
	// 			.groupType("회식")
	// 			.preferredLocation("서울대입구")
	// 			.preferredMenu("치킨")
	// 			.preferredPrice(10000)
	// 			.build();
	// 		auctions.add(auction);
	// 	}
	//
	// 	auctionRepository.saveAll(auctions);
	// }
	//
	// @Test
	// public void findAllByTitleTest() {
	// 	Auction auction = auctionRepository.findAllByTitle("경매2").get(0);
	//
	// 	assertEquals(auction.getPreferredMenu(), "치킨");
	// }
	//
	// @Test
	// public void deleteById(){
	// 	auctionRepository.deleteById("1");
	// 	assertTrue(auctionRepository.findById("1").isEmpty());
	// }
	//
	// @Test
	// public void IncludedKeywordSearchTest() {
	// 	List<Auction> auctions = auctionRepository.findAllByTitleContaining("경");
	//
	// 	assertEquals(auctions.size(), 10);
	// }
	//
	// @Test
	// public void NotIncludedKeywordSearchTest() {
	// 	List<Auction> auctions2 = auctionRepository.findAllByTitleContaining("경매2222222");
	//
	// 	assertTrue(auctions2.isEmpty());
	// }

}

