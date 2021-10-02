package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.criteria.internal.expression.SubqueryComparisonModifierExpression;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SuccessBidService {
	private final SuccessBidRepository successBidRepository;
	private final AuctionRepository auctionRepository;
	private final AuctioneerRepository auctioneerRepository;

	public SuccessBid writeSuccessBid(long auctionId, long auctioneerId) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
		auction.setStatus(AuctionStatus.END);
		Auctioneer auctioneer = auctioneerRepository.findById(auctioneerId).orElseThrow(ResourceNotExistException::new);
		auctioneer.setSuccess(true);
		SuccessBid successBid = SuccessBid.builder()
			.auctionId(auction.getAuctionId())
			.isComplete(true)
			.storeId(auctioneer.getStoreId())
			.userId(auction.getUserId())
			.menu(auctioneer.getMenu())
			.price(auctioneer.getPrice())
			.groupCnt(auction.getGroupCnt())
			.reservation(auction.getReservation())
			.groupType(auction.getGroupType())
			.storeName(auctioneer.getStoreName())
			.path(auctioneer.getPath())
			.build();

		log.info(String.valueOf(successBid));
		SuccessBid successBid1= successBidRepository.save(successBid);
		successBid1.setComplete(true);

		log.info(String.valueOf(successBid1));
		return successBid1;
	}

	public List<SuccessBid> getSuccessbidsByUser(UserIdDto user) {
		if(user.getType().equals("CUSTOMER")) {
			return successBidRepository.findAllByUserId(user.getId());
		}else{
			return successBidRepository.findAllByStoreId(user.getId());
		}
	}


	public Auction findAuctionBySuccessBidId(long successBidId){
		long auctionId =successBidRepository.findAuctionBySuccessBidId(successBidId);
		Auction auction=auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
		return auction;
	}

}
