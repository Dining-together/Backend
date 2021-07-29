package kr.or.dining_together.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.dto.StoreDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public StoreDto send(String topic, StoreDto storeDto) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(storeDto);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Kafka Producer sent data from the Member microservice: " + storeDto);

		return storeDto;
	}
}
