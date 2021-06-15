package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.entity.StoreFavorites;
import kr.or.dining_together.member.jpa.repo.CustomerFavoritesRepository;
import kr.or.dining_together.member.jpa.repo.StoreFavoritesRepository;
import kr.or.dining_together.member.vo.FavoritesRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritesService {

	private final StoreFavoritesRepository storeFavoritesRepository;
	private final CustomerFavoritesRepository customerFavoritesRepository;
	private final UserService userService;

	public List<CustomerFavorites> getCustomerFavoritesAll(String email) {
		Long userId = userService.getUserId(email);
		List<CustomerFavorites> customerFavorites = customerFavoritesRepository.findAllByUserId(userId);
		return customerFavorites;
	}

	public List<StoreFavorites> getStoreFavoritesAll(String email) {
		Long userId = userService.getUserId(email);
		List<StoreFavorites> storeFavorites = storeFavoritesRepository.findAllByUserId(userId);
		return storeFavorites;
	}

	public void saveFavorites(String email, FavoritesRequest favoritesRequest) {
		Long userId = userService.getUserId(email);
		String requestType = favoritesRequest.getFavoritesType();
		Long objectId = favoritesRequest.getObjectId();

		Long saveResult = null;
		if (requestType.equals("STORE")) {
			CustomerFavorites customerFavorites = CustomerFavorites.builder()
				.userId(userId)
				.storeId(objectId)
				.build();
			customerFavoritesRepository.save(customerFavorites);
			saveResult = customerFavoritesRepository.findByStoreId(objectId).getId();
		} else if (requestType.equals("AUCTION")) {
			StoreFavorites storeFavorites = StoreFavorites.builder()
				.userId(userId)
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

	public void deleteFavorite(String email, FavoritesRequest favoritesRequest) {
		Long userId = userService.getUserId(email);
		String requestType = favoritesRequest.getFavoritesType();
		Long objectId = favoritesRequest.getObjectId();

		Long deleteResult = null;
		if (requestType.equals("STORE")) {
			deleteResult = customerFavoritesRepository.deleteByUserIdAndStoreId(userId, objectId);
		} else if (requestType.equals("AUCTION")) {
			deleteResult = storeFavoritesRepository.deleteByUserIdAndAuctionId(userId, objectId);
		}

		if (deleteResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}
}
