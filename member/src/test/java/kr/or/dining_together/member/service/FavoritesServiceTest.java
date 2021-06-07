package kr.or.dining_together.member.service;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Favorites;
import kr.or.dining_together.member.jpa.repo.FavoritesRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class FavoritesServiceTest {

	@Autowired
	FavoritesService favoritesService;

	@Autowired
	FavoritesRepository favoritesRepository;

	@Autowired
	UserRepository userRepository;

	@Before
	public void setUp() {
		Favorites favorites = Favorites.builder()
			.userId(1L)
			.objectId(1L)
			.build();

		Customer customer = Customer.builder()
			.email("qja9605@naver.com")
			.name("신태범")
			.password("test1111")
			.roles(Collections.singletonList("ROLE_USER"))
			.dateOfBirth("1996-05-04")
			.phoneNo("010-2691-3895")
			.gender("MALE")
			.build();

		favoritesRepository.save(favorites);
		userRepository.save(customer);
	}

	@Test
	public void save() {
		//given
		String email = "qja9605@google.com";
		long objectId = 2;

		//when
		favoritesService.saveFavorite(email, objectId);
		Optional<Favorites> favorites = favoritesRepository.findByObjectId(objectId);

		//then
		assertNotNull(favorites);
	}

	@Test
	public void get() {
		//given
		String email = "qja9605@naver.com";

		//when
		List<Favorites> userFavorites = favoritesService.getFavoritesAll(email);

		//then
		assertEquals(userFavorites.get(0).getObjectId(), 1L);
	}

	@Test
	public void delete() {
		//given
		String email = "qja9605@naver.com";
		long objectId = 1;

		//when
		favoritesService.deleteFavorite(email, objectId);

		//then
		assertNull(favoritesService.getFavoritesAll(email).get(0));
	}
}
