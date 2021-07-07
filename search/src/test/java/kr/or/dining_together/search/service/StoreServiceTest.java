package kr.or.dining_together.search.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.search.document.Store;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Before
	public void setup() {
		List<Store> stores = new ArrayList();

		for (int i = 1; i <= 10; i++) {
			String storeId = String.valueOf(i);
			Store store = new Store().builder()
				.id(storeId)
				.title("bbq치킨")
				.description("마시따")
				.storeType("치킨")
				.Latitude("37.5665")
				.Longitude("126.734086")
				.bookmark(false)
				.reviewScore("0")
				.build();

			stores.add(store);
		}

		storeService.createStoreIndexBulk(stores);
	}

	@Test
	public void createOneIndexAndDelete() throws IOException {
		Store store = new Store().builder()
			.id("11")
			.title("도미노피자")
			.description("마시따")
			.storeType("피자")
			.Latitude("37.5665")
			.Longitude("126.734086")
			.bookmark(false)
			.reviewScore("0")
			.build();

		storeService.createStoreIndex(store);
		assertFalse(storeService.findByTitle("도미노피자").isEmpty());

		storeService.deleteStoreDocument("11");
		assertTrue(storeService.findByTitle("도미노피자").isEmpty());

	}

	@Test
	public void findAllByTitle() {
		Store store = storeService.findByTitle("bbq치킨").get(0);

		System.out.println(store);
	}

	@Test
	public void IncludedKeywordSearchTest() {
		List<Store> stores = storeService.findByTitleMatchingNames("치킨");

		assertEquals(stores.size(), 10);
	}

	@Test
	public void NotIncludedKeywordSearchTest() {
		List<Store> stores2 = storeService.findByTitleMatchingNames("경매2222222");

		assertTrue(stores2.isEmpty());
	}

}
