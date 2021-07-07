package kr.or.dining_together.member.service;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.repo.CustomerFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.StoreFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.FavoritesRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class FavoritesServiceTest {

	@Autowired
	FavoritesService favoritesService;

	@Autowired
	StoreFavoritesRepository storeFavoritesRepository;

	@Autowired
	CustomerFavoritesRepository customerFavoritesRepository;

	@Autowired
	UserRepository userRepository;

	String email = "qja9605@naver.com";

	@Before
	public void setUp() {
		Customer customer = Customer.builder()
			.email(email)
			.name("신태범")
			.password("test1111")
			.age(25)
			.gender("MALE")
			.provider("application")
			.roles(Collections.singletonList("ROLE_USER"))
			.build();

		userRepository.save(customer);
	}

	@Test
	public void save() throws Throwable {
		//given
		FavoritesRequest favoritesRequest = FavoritesRequest.builder()
			.favoritesType("STORE")
			.objectId(1L)
			.build();

		//when
		favoritesService.saveFavorites(email, favoritesRequest);
		CustomerFavorites favorites = customerFavoritesRepository.findByStoreId(1L);

		//then
		System.out.println(favorites);
		assertNotNull(favorites);
	}

	@Test
	public void get() throws Throwable {
		//given
		String email = "qja9605@naver.com";

		//when
		List<CustomerFavorites> userFavorites = favoritesService.getCustomerFavoritesAll(email);

		//then
		assertNotNull(userFavorites);
	}

	@Test
	public void delete() throws Throwable {
		//given
		String email = "qja9605@naver.com";
		FavoritesRequest favoritesRequest = FavoritesRequest.builder()
			.favoritesType("STORE")
			.objectId(1L)
			.build();

		//when
		favoritesService.deleteFavorite(email, favoritesRequest);

		//then
		assertTrue(favoritesService.getCustomerFavoritesAll(email).isEmpty());
	}
}
