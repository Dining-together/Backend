// package kr.or.dining_together.member.jpa.repo;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.junit4.SpringRunner;
//
// import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
// import kr.or.dining_together.member.jpa.entity.StoreFavorites;
//
// @RunWith(SpringRunner.class)
// @DataJpaTest
// public class FavoritesRepositoryTest {
// 	@Autowired
// 	CustomerFavoritesRepository customerFavoritesRepository;
//
// 	@Autowired
// 	StoreFavoritesRepository storeFavoritesRepository;
//
// 	@Before
// 	@Test
// 	public void setUp() {
// 		CustomerFavorites customerFavorites = CustomerFavorites.builder()
// 			.userId(1L)
// 			.storeId(1L)
// 			.build();
// 		customerFavoritesRepository.save(customerFavorites);
//
// 		StoreFavorites storeFavorites = StoreFavorites.builder()
// 			.userId(2L)
// 			.auctionId(1L)
// 			.build();
//
// 		storeFavoritesRepository.save(storeFavorites);
// 	}
//
// 	@Test
// 	public void find() {
// 		CustomerFavorites customerFavorite = customerFavoritesRepository.findByStoreId(1L);
// 		StoreFavorites storeFavorite = storeFavoritesRepository.findByAuctionId(1L);
//
// 		assertNotNull(customerFavorite);
// 		assertNotNull(storeFavorite);
// 	}
//
// 	@Test
// 	public void delete() {
// 		//given
// 		CustomerFavorites customerFavorites1 = customerFavoritesRepository.findByStoreId(1L);
// 		assertNotNull(customerFavorites1);
//
// 		StoreFavorites storeFavorites1 = storeFavoritesRepository.findByAuctionId(1L);
// 		assertNotNull(storeFavorites1);
//
// 		//when
// 		customerFavoritesRepository.deleteByUserIdAndStoreId(1L, 1L);
// 		storeFavoritesRepository.deleteByUserIdAndAuctionId(2L, 1L);
//
// 		//then
// 		CustomerFavorites customerFavorites2 = customerFavoritesRepository.findByStoreId(1L);
// 		assertNull(customerFavorites2);
//
// 		StoreFavorites storeFavorites2 = storeFavoritesRepository.findByAuctionId(1L);
// 		assertNull(storeFavorites2);
// 	}
// }
