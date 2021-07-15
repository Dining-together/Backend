package kr.or.dining_together.auction.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.dto.Field;
import kr.or.dining_together.auction.dto.KafkaAuctionDto;
import kr.or.dining_together.auction.dto.Payload;
import kr.or.dining_together.auction.dto.Schema;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuctionProducer {
	private KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = Arrays.asList(new Field("string", true, "auction_id"),
		new Field("string", true, "title"),
		new Field("string", true, "userType"),
		new Field("date", true, "reservation"));

	Schema schema = Schema.builder()
		.type("struct")
		.fields(fields)
		.optional(false)
		.name("auction")
		.build();

	@Autowired
	public AuctionProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public AuctionDto send(String topic, AuctionDto AuctionDto) {
		Payload payload = Payload.builder()
			.id(AuctionDto.getAuctionId())
			.userType(AuctionDto.getUserType().toString())
			.title(AuctionDto.getTitle())
			.reservation(AuctionDto.getReservation())
			.build();

		KafkaAuctionDto kafkaAuctionDto = new KafkaAuctionDto(schema, payload);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaAuctionDto);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Order Producer sent data from the Order microservice: " + kafkaAuctionDto);

		return AuctionDto;
	}
}
