package kr.or.dining_together.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaTemplate<String, StoreDto> storeKafkaTemplate;

	public void send(String topicName,StoreDto storeDto)
	{
		ListenableFuture<SendResult<String, StoreDto>> future
			= this.storeKafkaTemplate.send(topicName, storeDto);

		future.addCallback(new ListenableFutureCallback<SendResult<String, StoreDto>>() {
			@Override
			public void onSuccess(SendResult<String, StoreDto> result) {
				log.info("User created: "
					+ storeDto + " with offset: " + result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("User created : " + storeDto, ex);
			}
		});
	}

}
