package kr.or.dining_together.search.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.repository.AuctionRepository;
import kr.or.dining_together.search.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {
	StoreRepository storeRepository;
	AuctionRepository auctionRepository;

	@Autowired
	public KafkaConsumer(StoreRepository storeRepository, AuctionRepository auctionRepository) {
		this.storeRepository = storeRepository;
		this.auctionRepository = auctionRepository;
	}

	@KafkaListener(topics = "member-store-topic")
	public void updateStore(String kafkaMessage) {
		log.info("Kafka Message: ->" + kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
			});
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		Store store = Store.builder()
			.id((String)map.get("storeId"))
			.title((String)map.get("storeName"))
			.addr((String)map.get("addr"))
			.storeType((String)map.get("storeType"))
			.build();

		storeRepository.save(store);
	}

	@KafkaListener(topics = "auction-auction-topic")
	public void updateAuction(String kafkaMessage) {
		log.info("Kafka Message: ->" + kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
			});
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		Auction auction = Auction.builder()
			.reservation(LocalDateTime.parse((String)map.get("reservation")))
			.userType((String)map.get("userType"))
			.title((String)map.get("title"))
			.build();

		auctionRepository.save(auction);
	}
}