package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.jpa.entity.Favorites;
import kr.or.dining_together.member.jpa.repo.FavoritesRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritesService {

	private FavoritesRepository favoritesRepository;
	private UserRepository userRepository;
	private UserService userService;

	public List<Favorites> getFavoritesAll(String email) {
		Long userId = userService.getUserId(email);
		List<Favorites> userFavorites = favoritesRepository.findAllByUserId(userId);
		return userFavorites;
	}

	public void saveFavorite(String email, long objectId) {
		Long userId = userService.getUserId(email);
		Favorites favorites = Favorites.builder()
			.userId(userId)
			.objectId(objectId)
			.build();
		Long saveResult = favoritesRepository.save(favorites).getId();
		if (saveResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}

	public void deleteFavorite(String email, long objectId) {
		Long userId = userService.getUserId(email);
		Long deleteResult = favoritesRepository.deleteByUserIdAndObjectId(userId, objectId);
		if (deleteResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}
}
