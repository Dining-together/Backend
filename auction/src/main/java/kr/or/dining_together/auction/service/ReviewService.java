package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.NotCompletedException;
import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.advice.exception.UserNotMatchedException;
import kr.or.dining_together.auction.dto.ReviewDto;
import kr.or.dining_together.auction.dto.ReviewScoreDto;
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
	private final AuctionKafkaProducer auctionProducer;
	@Value(value = "${kafka.topic.review.name}")
	private String REVIEW_KAFKA_TOPIC;

	@Transactional
	public Review writeReview(ReviewDto reviewDto, long successBidId, UserIdDto userIdDto) {
		SuccessBid successBid = successBidRepository.findById(successBidId).orElseThrow(ResourceNotExistException::new);

		if (successBid.getUserId() != userIdDto.getId()) {
			throw new UserNotMatchedException();
		}
		if (successBid.isComplete() == false) {
			throw new NotCompletedException();
		}
		Review review = Review.builder()
			.storeId(successBid.getStoreId())
			.content(reviewDto.getContent())
			.userName(userIdDto.getName())
			.userId(userIdDto.getId())
			.score(reviewDto.getScore())
			.build();
		review = reviewRepository.save(review);
		// 리뷰 평점 개수 구하는 부분
		successBid.setReview(true);
		int reviewCnt = Math.toIntExact(reviewRepository.getReviewCntByStoreId(successBid.getStoreId()));
		Double reviewAvg = reviewRepository.getReviewAvgByStoreId(successBid.getStoreId());


		log.info(String.valueOf(reviewCnt));
		log.info(String.valueOf(reviewAvg));

		ReviewScoreDto reviewScoreDto = ReviewScoreDto.builder()
			.storeId(review.getStoreId())
			.reviewCnt(reviewCnt)
			.reviewAvg(reviewAvg)
			.build();

		auctionProducer.sendReviewScoreDto(REVIEW_KAFKA_TOPIC, reviewScoreDto);

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
