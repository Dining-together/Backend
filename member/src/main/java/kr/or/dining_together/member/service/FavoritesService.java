package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.jpa.entity.Favorites;
import kr.or.dining_together.member.jpa.repo.FavoritesRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.FavoritesRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritesService {

	private final FavoritesRepository favoritesRepository;
	private final UserRepository userRepository;
	private final UserService userService;

	public List<Favorites> getFavoritesAll(String email) {
		Long userId = userService.getUserId(email);
		List<Favorites> userFavorites = favoritesRepository.findAllByUserId(userId);
		return userFavorites;
	}

	public void saveFavorite(String email, FavoritesRequest favoritesRequest) {
		Long userId = userService.getUserId(email);
		String requestType = favoritesRequest.getFavoritesType();
		Long objectid = favoritesRequest.getObjectid();

		Favorites favorites = new Favorites();
		if (requestType.equals("STORE")) {
			favorites = Favorites.builder()
				.userId(userId)
				.storeId(objectid)
				.build();
		} else if (requestType.equals("AUCTION")) {
			favorites = Favorites.builder()
				.userId(userId)
				.auctionId(objectid)
				.build();
		}

		Long saveResult = favoritesRepository.save(favorites).getId();
		if (saveResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}

	public void deleteFavorite(String email, FavoritesRequest favoritesRequest) {
		Long userId = userService.getUserId(email);
		String requestType = favoritesRequest.getFavoritesType();
		Long objectId = favoritesRequest.getObjectid();

		Long deleteResult = null;
		if (requestType.equals("STORE")) {
			deleteResult = favoritesRepository.deleteByUserIdAndStoreId(userId, objectId);
		} else if (requestType.equals("AUCTION")) {
			deleteResult = favoritesRepository.deleteByUserIdAndAuctionId(userId, objectId);
		}

		if (deleteResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}
}
