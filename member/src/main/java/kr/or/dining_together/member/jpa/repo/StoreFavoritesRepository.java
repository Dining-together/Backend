package kr.or.dining_together.member.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.StoreFavorites;

public interface StoreFavoritesRepository extends JpaRepository<StoreFavorites, Long> {
	@Override
	Optional<StoreFavorites> findById(Long id);

	List<StoreFavorites> findAllByStoreId(long storeId);

	StoreFavorites findByAuctionId(Long storeId);

	@Transactional
	@Modifying
	Long deleteByStoreIdAndAuctionId(Long storeId, Long auctionId);
}
