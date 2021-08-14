package kr.or.dining_together.search.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

		Optional<Auction> originalAuction = auctionRepository.findById(auctionDto.getAuctionId());

		Auction auction = Auction.builder()
			.id(auctionDto.getAuctionId())
			.userId(auctionDto.getUserId())
			.title(auctionDto.getTitle())
			.content(auctionDto.getContent())
			.userName(auctionDto.getUserName())
			.imagePath(auctionDto.getPath())
			.reservation(LocalDateTime.parse(auctionDto.getReservation(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.userType(auctionDto.getUserType())
			.deadLine(
				LocalDateTime.parse(auctionDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.maxPrice(auctionDto.getMaxPrice())
			.minPrice(auctionDto.getMinPrice())
			.storeType(auctionDto.getStoreType())
			.build();

		if(originalAuction.isEmpty()) {
			auctionRepository.save(auction);
			log.info(String.format("AuctionDto saved -> %s", auction));
		}else{
			originalAuction.get().setUpdate(auctionDto.getTitle(), auctionDto.getContent(), auctionDto.getUserName(),
				auctionDto.getPath(),auctionDto.getStoreType(), auctionDto.getMinPrice(),
				auctionDto.getMaxPrice(), auction.getReservation(), auction.getDeadLine(),
				auctionDto.getUserType());
			auctionRepository.save(originalAuction.get());
			log.info(String.format("AuctionDto updated -> %s", auction));
		}
	}

	@KafkaListener(topics = "${kafka.topic.store.name}",
		groupId = "${kafka.topic.store.id}",
		containerFactory = "storeKafkaListenerContainerFactory")
	public void consumeStoreTopic(StoreDto storeDto) {
		log.info(String.format("StoreDto received -> %s", storeDto));
		System.out.println(storeDto);
		Optional<Store> originalStore=storeRepository.findById(storeDto.getStoreId());

		Store store = Store.builder()
			.id(storeDto.getStoreId())
			.title(storeDto.getStoreName())
			.comment(storeDto.getComment())
			.addr(storeDto.getAddr())
			.storeType(storeDto.getStoreType())
			.openTime(
				LocalDateTime.parse(storeDto.getOpenTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.closedTime(
				LocalDateTime.parse(storeDto.getClosedTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.longitude(Double.toString(storeDto.getLongitude()))
			.latitude(Double.toString(storeDto.getLatitude()))
			.phoneNum(storeDto.getPhoneNum())
			.storeImagePath(storeDto.getStoreImagePath())
			.build();
		if(originalStore.isPresent()){
			originalStore.get().update(store.getTitle(),store.getPhoneNum(), store.getAddr(), store.getLatitude(),
				store.getLongitude(), store.getComment(), store.getStoreType(),
				store.getOpenTime(), store.getClosedTime(),store.getStoreImagePath());
			storeRepository.save(originalStore.get());
			log.info(String.format("StoreDto updated -> %s", storeDto));
		}else{
			storeRepository.save(store);
			log.info(String.format("StoreDto saved -> %s", storeDto));
		}
	}
}