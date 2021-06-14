package kr.or.dining_together.member.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Favorites;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

	@Override
	Optional<Favorites> findById(Long id);

	List<Favorites> findAllByUserId(Long userId);

	Favorites findByAuctionId(Long auctionId);

	Favorites findByStoreId(Long storeId);
	
	@Transactional
	@Modifying
	Long deleteByUserIdAndAuctionId(Long userId, Long auctionId);

	@Transactional
	@Modifying
	Long deleteByUserIdAndStoreId(Long userId, Long storeId);
}
