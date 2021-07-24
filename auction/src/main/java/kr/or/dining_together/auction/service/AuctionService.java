package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.vo.AuctionRequest;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.auction.service
 * @name: AuctionService.java
 * @date : 2021/06/06 4:14 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
	private final AuctionRepository auctionRepository;

	public List<Auction> getAuctions() {
		List<Auction> auctions = auctionRepository.findAll();
		return auctions;
	}

	public List<Auction> getAuctionsByProceeding() {
		List<Auction> auctions = auctionRepository.findAllByStatus(AuctionStatus.PROCEEDING);
		return auctions;
	}

	public List<Auction> getAuctionsByEnd() {
		List<Auction> auctions = auctionRepository.findAllByStatus(AuctionStatus.END);
		return auctions;
	}

	public Auction getAuction(long auctionId) {
		return auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
	}

	public List<Auction> getAuctionsByUserId(long userId) {
		return auctionRepository.findAllByUserId(userId);
	}

	public Auction writeAuction(UserIdDto user, AuctionRequest auctionRequest) {
		Auction auction = Auction.builder()
			.content(auctionRequest.getContent())
			.title(auctionRequest.getTitle())
			.storeType(auctionRequest.getStoreType())
			.deadline(auctionRequest.getDeadline())
			.maxPrice(auctionRequest.getMaxPrice())
			.minPrice(auctionRequest.getMinPrice())
			.userId(user.getId())
			.userName(user.getName())
			.groupType(auctionRequest.getGroupType())
			.groupCnt(auctionRequest.getGroupCnt())
			.reservation(auctionRequest.getReservation())
			.build();

		return auctionRepository.save(auction);
	}

	public Auction updateAuction(long auctionId, AuctionRequest auctionRequest) {
		Auction auction = getAuction(auctionId);
		auction.setUpdate(auctionRequest.getTitle(), auctionRequest.getContent(), auctionRequest.getStoreType(),
			auctionRequest.getMinPrice(),
			auctionRequest.getGroupType(), auctionRequest.getGroupCnt(), auctionRequest.getMaxPrice(),
			auctionRequest.getReservation(),
			auctionRequest.getDeadline());
		return auction;
	}

	public boolean deleteAuction(long auctionId) {
		Auction auction = getAuction(auctionId);
		auctionRepository.delete(auction);
		return true;
	}

}
