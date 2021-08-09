package kr.or.dining_together.auction.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.or.dining_together.auction.jpa.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByUserId(long userId);

	List<Review> findAllByStoreId(long store);

	Review findReviewByReviewId(long reviewId);

	@Query("select AVG(r.score) from Review r where r.storeId=:storeId ")
	double getReviewAvgByStoreId(long storeId);

	@Query("select COUNT(r.score) from Review r where r.storeId=:storeId ")
	int getReviewCntByStoreId(long storeId);
}
