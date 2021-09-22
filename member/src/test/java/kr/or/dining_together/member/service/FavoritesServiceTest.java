package kr.or.dining_together.member.service;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreFavorites;
import kr.or.dining_together.member.jpa.repo.CustomerFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.StoreFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.FavoritesRequest;

@PropertySource("classpath:application.yml")
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
	CustomerFavorites customerFavorites;
	StoreFavorites storeFavorites;
	Customer user;
	Store user1;
	String email = "qja9605@naver.com";
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Before
	public void setUp() {
		//given
		String email = "jifrozen@naver.com";
		String name = "문지언";
		Store store = Store.builder()
			.email(email)
			.name(name)
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.documentChecked(true)
			.build();

		String email2 = "qja9605@naver.com";
		String name2 = "신태범";
		Customer customer = Customer.builder()
			.email(email2)
			.name(name2)
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.age(23)
			.gender("MALE")
			.build();
		//when
		user1 = (Store)userRepository.save(store);
		user = (Customer)userRepository.save(customer);

		customerFavorites = CustomerFavorites.builder()
			.customer(user)
			.storeId(1L)
			.build();
		customerFavoritesRepository.save(customerFavorites);
		storeFavorites = StoreFavorites.builder()
			.store(user1)
			.auctionId(1L)
			.build();

		storeFavoritesRepository.save(storeFavorites);
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
