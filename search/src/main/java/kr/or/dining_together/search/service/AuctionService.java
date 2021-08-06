package kr.or.dining_together.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private static final String AUCTION_INDEX = "auctions";

	private final AuctionRepository auctionRepository;

	public void createAuctionIndex(Auction auction) {
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
		return auctionRepository.findAllByTitleContaining(title);
	}

}
