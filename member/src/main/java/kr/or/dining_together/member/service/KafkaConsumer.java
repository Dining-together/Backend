package kr.or.dining_together.member.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.ReviewScoreDto;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final StoreRepository storeRepository;

	@KafkaListener(topics = "${kafka.topic.review.name}",
		groupId = "${kafka.topic.review.id}",
		containerFactory = "reviewKafkaListenerContainerFactory")
	public void consume(ReviewScoreDto reviewScoreDto) {
		log.info(String.format("ReviewScoreDto recieved -> %s", reviewScoreDto));

		Store store = storeRepository.findById(reviewScoreDto.getStoreId()).orElseThrow(UserNotFoundException::new);

		store.setReviewCnt(reviewScoreDto.getReviewCnt());
		store.setReviewAvg(reviewScoreDto.getReviewAvg());
	}
}
