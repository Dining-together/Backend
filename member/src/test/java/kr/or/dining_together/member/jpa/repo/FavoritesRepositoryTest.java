package kr.or.dining_together.member.jpa.repo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.Favorites;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FavoritesRepositoryTest {
	@Autowired
	FavoritesRepository favoritesRepository;

	@Before
	@Test
	public void setUp() {
		Favorites favorites = Favorites.builder()
			.userId(1L)
			.storeId(1L)
			.build();
		favoritesRepository.save(favorites);
	}

	@Test
	public void delete() {
		//given
		Favorites favorites1 = favoritesRepository.findByStoreId(1L);
		assertTrue(favorites1.getUserId() > 0);

		//when
		favoritesRepository.deleteByUserIdAndStoreId(1L, 1L);

		//then
		Favorites favorites2 = favoritesRepository.findByStoreId(1L);
		assertNull(favorites2);
	}
}
