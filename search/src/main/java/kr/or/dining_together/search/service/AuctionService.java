package kr.or.dining_together.search.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.search.advice.exception.ResourceNotExistException;
import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.dto.AuctionDto;
import kr.or.dining_together.search.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

	private final AuctionRepository auctionRepository;

	public void createAuctionIndex(AuctionDto auctionDto) {
		Auction auction = Auction.builder()
			.id(auctionDto.getAuctionId())
			.title(auctionDto.getTitle())
			.content(auctionDto.getContent())
			.userName(auctionDto.getUserName())
			.reservation(LocalDateTime.parse(auctionDto.getReservation(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.userType(auctionDto.getUserType())
			.deadLine(
				LocalDateTime.parse(auctionDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.maxPrice(auctionDto.getMaxPrice())
			.minPrice(auctionDto.getMinPrice())
			.storeType(auctionDto.getStoreType())
			.build();

		auctionRepository.save(auction);
	}

	// public void createAuctionIndexBulk(List<Auction> auctions) {
	// 	auctionRepository.saveAll(auctions);
	// }

	// public void deleteAuctionDocument(String id) throws IOException {
	// 	auctionRepository.deleteById(id);
	// }

	// public List<Auction> findByTitle(final String title) {
	// 	return auctionRepository.findAllByTitle(title);
	// }

	public List<Auction> findByTitleMatchingNames(final String title) {
		List<Auction> auctions = auctionRepository.findAllByTitleContaining(title);

		if (auctions.isEmpty()) {
			String message = "search result is not exist";
			log.info("error-log :: {}", message);
			throw new ResourceNotExistException();
		}

		return auctions;
	}

}
