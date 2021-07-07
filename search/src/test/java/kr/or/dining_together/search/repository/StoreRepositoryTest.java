package kr.or.dining_together.search.repository;

import static org.junit.jupiter.api.Assertions.*;

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
public class StoreRepositoryTest {

	@Autowired
	private StoreRepository storeRepository;

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

		storeRepository.saveAll(stores);
	}

	@Test
	public void deleteById(){
		storeRepository.deleteById("1");
		assertTrue(storeRepository.findById("1").isEmpty());
	}

	@Test
	public void findAllByTitle() {
		Store store = storeRepository.findAllByTitle("bbq치킨").get(0);

		System.out.println(store);
	}

	@Test
	public void IncludedKeywordSearchTest() {
		List<Store> stores = storeRepository.findAllByTitleContaining("치킨");

		assertEquals(stores.size(), 10);
	}

	@Test
	public void NotIncludedKeywordSearchTest() {
		List<Store> stores2 = storeRepository.findAllByTitleContaining("경매2222222");

		assertTrue(stores2.isEmpty());
	}
}
