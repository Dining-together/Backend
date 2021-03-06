package kr.or.dining_together.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.dto.ReviewScoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionKafkaProducer {

	private final KafkaTemplate<String, AuctionDto> auctionKafkaTemplate;
	private final KafkaTemplate<String, ReviewScoreDto> reviewScoreDtoKafkaTemplate;

	public void send(String topicName, AuctionDto auctionDto) {
		ListenableFuture<SendResult<String, AuctionDto>> future
			= this.auctionKafkaTemplate.send(topicName, auctionDto);

		future.addCallback(new ListenableFutureCallback<SendResult<String, AuctionDto>>() {
			@Override
			public void onSuccess(SendResult<String, AuctionDto> result) {
				log.info("User created: "
					+ auctionDto + " with offset: " + result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("User created : " + auctionDto, ex);
			}
		});
	}

	public void sendReviewScoreDto(String topicName, ReviewScoreDto reviewScoreDto) {
		ListenableFuture<SendResult<String, ReviewScoreDto>> future
			= this.reviewScoreDtoKafkaTemplate.send(topicName, reviewScoreDto);

		future.addCallback(new ListenableFutureCallback<SendResult<String, ReviewScoreDto>>() {
			@Override
			public void onSuccess(SendResult<String, ReviewScoreDto> result) {
				log.info("User created: "
					+ reviewScoreDto + " with offset: " + result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("User created : " + reviewScoreDto, ex);
			}
		});
	}
}
