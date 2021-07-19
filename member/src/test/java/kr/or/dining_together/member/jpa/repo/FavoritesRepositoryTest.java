package kr.or.dining_together.member.jpa.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreFavorites;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FavoritesRepositoryTest {
	@Autowired
	CustomerFavoritesRepository customerFavoritesRepository;

	@Autowired
	StoreFavoritesRepository storeFavoritesRepository;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	UserRepository userRepository;
	CustomerFavorites customerFavorites;
	StoreFavorites storeFavorites;
	Customer user;
	Store user1;
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

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void find() {
		CustomerFavorites customerFavorite = customerFavoritesRepository.findByStoreId(1L);
		StoreFavorites storeFavorite = storeFavoritesRepository.findByAuctionId(1L);

		assertNotNull(customerFavorite);
		assertNotNull(storeFavorite);
	}

	@Test
	public void delete() {
		//given
		CustomerFavorites customerFavorites1 = customerFavoritesRepository.findByStoreId(1L);
		assertNotNull(customerFavorites1);

		StoreFavorites storeFavorites1 = storeFavoritesRepository.findByAuctionId(1L);
		assertNotNull(storeFavorites1);

		Optional<Store> store = storeRepository.findById(user1.getId());
		Optional<Customer> customer = customerRepository.findById(user.getId());
		//when
		customerFavoritesRepository.deleteByCustomerIdAndStoreId(customer.get().getId(), 1L);
		storeFavoritesRepository.deleteByStoreIdAndAuctionId(store.get().getId(), 1L);

		//then
		CustomerFavorites customerFavorites2 = customerFavoritesRepository.findByStoreId(1L);
		assertNull(customerFavorites2);

		StoreFavorites storeFavorites2 = storeFavoritesRepository.findByAuctionId(1L);
		assertNull(storeFavorites2);
	}
}
