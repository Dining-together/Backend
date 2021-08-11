package kr.or.dining_together.search.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.dto.AuctionDto;
import kr.or.dining_together.search.dto.StoreDto;
import kr.or.dining_together.search.repository.AuctionRepository;
import kr.or.dining_together.search.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
	private final StoreRepository storeRepository;
	private final AuctionRepository auctionRepository;

	@KafkaListener(topics = "${kafka.topic.auction.name}",
		groupId = "${kafka.topic.auction.id}",
		containerFactory = "auctionKafkaListenerContainerFactory")
	public void consumeAuctionTopic(AuctionDto auctionDto) {
		log.info(String.format("AuctionDto received -> %s", auctionDto));
		System.out.println(auctionDto);

		Auction auction = Auction.builder()
			.id(auctionDto.getAuctionId())
			.title(auctionDto.getTitle())
			.content(auctionDto.getContent())
			.userName(auctionDto.getUserName())
			.reservation(LocalDateTime.parse(auctionDto.getReservation(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.userType(auctionDto.getUserType())
			.deadLine(LocalDateTime.parse(auctionDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.maxPrice(auctionDto.getMaxPrice())
			.minPrice(auctionDto.getMinPrice())
			.storeType(auctionDto.getStoreType())
			.build();

		auctionRepository.save(auction);

		log.info(String.format("AuctionDto saved -> %s", auction));
	}

	@KafkaListener(topics = "${kafka.topic.store.name}",
		groupId = "${kafka.topic.store.id}",
		containerFactory = "storeKafkaListenerContainerFactory")
	public void consumeStoreTopic(StoreDto storeDto) {
		log.info(String.format("StoreDto received -> %s", storeDto));
		System.out.println(storeDto);

		Store store = Store.builder()
			.id(storeDto.getStoreId())
			.title(storeDto.getStoreName())
			.comment(storeDto.getComment())
			.addr(storeDto.getAddr())
			.storeType(storeDto.getStoreType())
			.openTime(LocalDateTime.parse(storeDto.getOpenTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.closedTime(LocalDateTime.parse(storeDto.getClosedTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.Longitude(Double.toString(storeDto.getLongitude()))
			.Latitude(Double.toString(storeDto.getLatitude()))
			.phoneNum(storeDto.getPhoneNum())
			.storeImagePath(storeDto.getStoreImagePath())
			.build();

		storeRepository.save(store);

		log.info(String.format("StoreDto saved -> %s", storeDto));
	}
}