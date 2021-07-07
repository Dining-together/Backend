package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreFavorites;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.CustomerFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.CustomerRepository;
import kr.or.dining_together.member.jpa.repo.StoreFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.FavoritesRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritesService {

	private final StoreFavoritesRepository storeFavoritesRepository;
	private final CustomerFavoritesRepository customerFavoritesRepository;
	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final StoreRepository storeRepository;
	private final UserService userService;

	public List<CustomerFavorites> getCustomerFavoritesAll(String email) throws Throwable {
		Customer user = customerRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		List<CustomerFavorites> customerFavorites = customerFavoritesRepository.findAllByCustomer(user.getId());
		return customerFavorites;
	}

	public List<StoreFavorites> getStoreFavoritesAll(String email) throws Throwable {
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		List<StoreFavorites> storeFavorites = storeFavoritesRepository.findAllByStore(user.getId());
		return storeFavorites;
	}

	public void saveFavorites(String email, FavoritesRequest favoritesRequest) throws Throwable {

		String requestType = favoritesRequest.getFavoritesType();
		Long objectId = favoritesRequest.getObjectId();

		Long saveResult = null;
		if (requestType.equals("STORE")) {
			Customer user = customerRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
			CustomerFavorites customerFavorites = CustomerFavorites.builder()
				.customer(user)
				.storeId(objectId)
				.build();
			customerFavoritesRepository.save(customerFavorites);
			saveResult = customerFavoritesRepository.findByStoreId(objectId).getId();
		} else if (requestType.equals("AUCTION")) {
			Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
			StoreFavorites storeFavorites = StoreFavorites.builder()
				.store(user)
				.auctionId(objectId)
				.build();
			storeFavoritesRepository.save(storeFavorites);
			saveResult = storeFavoritesRepository.findByAuctionId(objectId).getId();
		}

		if (saveResult == null) {
			throw new DataSaveFailedException();
		}

		return;
	}

	public void deleteFavorite(String email, FavoritesRequest favoritesRequest) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		String requestType = favoritesRequest.getFavoritesType();
		Long objectId = favoritesRequest.getObjectId();

		Long deleteResult = null;
		if (requestType.equals("STORE")) {
			deleteResult = customerFavoritesRepository.deleteByCustomerAndStoreId(user.getId(), objectId);
		} else if (requestType.equals("AUCTION")) {
			deleteResult = storeFavoritesRepository.deleteByStoreAndAuctionId(user.getId(), objectId);
		}

		if (deleteResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}
}
