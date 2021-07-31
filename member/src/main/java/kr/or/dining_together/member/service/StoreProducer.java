package kr.or.dining_together.member.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.member.dto.Field;
import kr.or.dining_together.member.dto.KafkaStoreDto;
import kr.or.dining_together.member.dto.Payload;
import kr.or.dining_together.member.dto.Schema;
import kr.or.dining_together.member.dto.StoreDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StoreProducer {
	List<Field> fields = Arrays.asList(new Field("string", true, "store_id"),
		new Field("string", true, "store_name"),
		new Field("string", true, "addr"),
		new Field("string", true, "store_type"));
	Schema schema = Schema.builder()
		.type("struct")
		.fields(fields)
		.optional(false)
		.name("store")
		.build();
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public StoreProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public StoreDto send(String topic, StoreDto storeDto) {
		Payload payload = Payload.builder()
			.storeType(storeDto.getStoreType())
			.addr(storeDto.getAddr())
			.id(storeDto.getStoreId())
			.storeName(storeDto.getStoreName())
			.build();

		KafkaStoreDto kafkaStoreDto = new KafkaStoreDto(schema, payload);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaStoreDto);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Order Producer sent data from the Order microservice: " + kafkaStoreDto);

		return storeDto;
	}
}
