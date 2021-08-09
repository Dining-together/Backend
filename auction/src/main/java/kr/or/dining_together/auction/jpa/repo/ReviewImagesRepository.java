package kr.or.dining_together.auction.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.Review;
import kr.or.dining_together.auction.jpa.entity.ReviewImages;

public interface ReviewImagesRepository extends JpaRepository<ReviewImages, Long> {

	boolean deleteAllByReview(Review review);

}
