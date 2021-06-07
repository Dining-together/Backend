package kr.or.dining_together.member.jpa.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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
			.objectId(1L)
			.build();
		favoritesRepository.save(favorites);
	}

	@Test
	public void delete() {
		//given
		Optional<Favorites> favorites1 = favoritesRepository.findByObjectId(1L);
		assertTrue(favorites1.isPresent());

		//when
		favoritesRepository.deleteByUserIdAndObjectId(1L, 1L);

		//then
		Optional<Favorites> favorites2 = favoritesRepository.findByObjectId(1L);
		assertEquals(true, favorites2.isEmpty());
	}
}
