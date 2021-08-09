package kr.or.dining_together.auction.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.NotCompletedException;
import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.advice.exception.UserNotMatchedException;
import kr.or.dining_together.auction.dto.ReviewDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Review;
import kr.or.dining_together.auction.jpa.entity.SuccessBid;
import kr.or.dining_together.auction.jpa.repo.ReviewRepository;
import kr.or.dining_together.auction.jpa.repo.SuccessBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final SuccessBidRepository successBidRepository;

	public Review writeReview(ReviewDto reviewDto, long successId, UserIdDto userIdDto) {
		Optional<SuccessBid> successBid = successBidRepository.findById(successId);

		if (successBid.get().getUserId() != userIdDto.getId()) {
			throw new UserNotMatchedException();
		}
		if (successBid.get().isComplete() == false) {
			throw new NotCompletedException();
		}
		Review review = Review.builder()
			.storeId(successBid.get().getStoreId())
			.content(reviewDto.getContent())
			.userName(userIdDto.getName())
			.userId(userIdDto.getId())
			.score(reviewDto.getScore())
			.build();
		review=reviewRepository.save(review);
		// 리뷰 평점 개수 구하는 부분

		long reviewCnt = reviewRepository.getReviewCntByStoreId(successBid.get().getStoreId());
		log.info(String.valueOf(reviewCnt));
		Optional<Double> reviewAvg = Optional.ofNullable(reviewRepository.getReviewAvgByStoreId(successBid.get().getStoreId()));
		log.info(String.valueOf(reviewAvg));
		return review;
	}

	public Review modifyReview(ReviewDto reviewDto, long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(ResourceNotExistException::new);
		review.updateReview(reviewDto.getContent(), review.getScore());
		return reviewRepository.save(review);
	}

	public List<Review> getReviewsByStore(long storeId) {
		return reviewRepository.findAllByStoreId(storeId);
	}

	public List<Review> getReviewsByUser(long userId) {
		return reviewRepository.findAllByUserId(userId);
	}

	public Review getReview(long reviewId) {
		return reviewRepository.findReviewByReviewId(reviewId);
	}

	public boolean deleteReview(long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(ResourceNotExistException::new);
		reviewRepository.delete(review);
		return true;
	}
}
