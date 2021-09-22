package kr.or.dining_together.member.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.ReviewScoreDto;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final StoreRepository storeRepository;

	@KafkaListener(topics = "${kafka.topic.review.name}",
		groupId = "${kafka.topic.review.id}",
		containerFactory = "reviewKafkaListenerContainerFactory")
	public void consume(ReviewScoreDto reviewScoreDto) {

		try{
			log.info(String.format("ReviewScoreDto received -> %s", reviewScoreDto));
			Store store = storeRepository.findById(reviewScoreDto.getStoreId()).orElseThrow(UserNotFoundException::new);
			store.updateReviewCntAndReviewAvg(reviewScoreDto.getReviewCnt(), reviewScoreDto.getReviewAvg());
			storeRepository.save(store);
			log.info("Store updated -> %s", store);
		}catch (Exception e){
			log.error("consume cause exception : "+e);
		}
	}
}
