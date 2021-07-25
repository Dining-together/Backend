package kr.or.dining_together.auction.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;
import kr.or.dining_together.auction.jpa.entity.SuccessBid;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.SuccessBidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateService {

	private final AuctionRepository auctionRepository;
	private final SuccessBidRepository successBidRepository;

	@Scheduled(cron = "10 * * * * *")
	@Transactional
	public void updateState() {
		LocalDateTime endDate = LocalDateTime.now();
		List<Auction> auctions = auctionRepository.findAllByStatusAndDeadlineAfter(AuctionStatus.PROCEEDING, endDate);
		List<SuccessBid> successBids = successBidRepository.findAllByCompleteFalseAndReservationAfter(endDate);
		auctions.forEach(auction -> {
			log.info(String.valueOf(auction.getDeadline()));
			auction.setStatus(AuctionStatus.END);
		});
		successBids.forEach(successBid -> {
			log.info(String.valueOf(successBid.getReservation()));
			successBid.setComplete(true);
		});

		log.info(String.valueOf(endDate));
		auctionRepository.saveAll(auctions);
		successBidRepository.saveAll(successBids);
	}

}
