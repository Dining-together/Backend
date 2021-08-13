package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.jpa.entity.SuccessBid;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.AuctioneerRepository;
import kr.or.dining_together.auction.jpa.repo.SuccessBidRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SuccessBidService {
	private final SuccessBidRepository successBidRepository;
	private final AuctionRepository auctionRepository;
	private final AuctioneerRepository auctioneerRepository;

	public SuccessBid writeSuccessBid(long auctionId, long auctioneerId) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
		auction.setStatus(AuctionStatus.END);
		Auctioneer auctioneer = auctioneerRepository.findById(auctioneerId).orElseThrow(ResourceNotExistException::new);
		SuccessBid successBid = SuccessBid.builder()
			.auctionId(auction.getAuctionId())
			.storeId(auctioneer.getStoreId())
			.userId(auction.getUserId())
			.menu(auctioneer.getMenu())
			.price(auctioneer.getPrice())
			.groupCnt(auction.getGroupCnt())
			.reservation(auction.getReservation())
			.groupType(auction.getGroupType())
			.storeName(auctioneer.getStoreName())
			.build();

		return successBidRepository.save(successBid);
	}

	public List<SuccessBid> getSuccessbidsByUser(UserIdDto user) {
		return successBidRepository.findAllByUserId(user.getId());
	}

	public List<SuccessBid> getSuccessbidsByStore(UserIdDto user) {
		return successBidRepository.findAllByStoreId(user.getId());
	}

	public Auction findAuctionBySuccessBidId(long successBidId){
		long auctionId =successBidRepository.findAuctionBySuccessBidId(successBidId);
		Auction auction=auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
		return auction;
	}

}
