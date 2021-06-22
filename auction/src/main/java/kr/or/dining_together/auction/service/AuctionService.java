package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.vo.RequestAuction;
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
		return auctionRepository.findAll();
	}

	public Auction getAuction(long auctionId) {
		return auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
	}

	public List<Auction> getAuctionsByUserId(long userId) {
		return auctionRepository.findAllByUserId(userId);
	}

	public Auction writeAuction(UserIdDto user, RequestAuction requestAuction) {
		Auction auction = Auction.builder()
			.content(requestAuction.getContent())
			.title(requestAuction.getTitle())
			.deadline(requestAuction.getDeadline())
			.maxPrice(requestAuction.getMaxPrice())
			.minPrice(requestAuction.getMinPrice())
			.userId(user.getId())
			.userType(requestAuction.getUserType())
			.reservation(requestAuction.getReservation())
			.build();

		return auctionRepository.save(auction);
	}

	public Auction updateAuction(long auctionId, RequestAuction requestAuction) {
		Auction auction = getAuction(auctionId);
		auction.setUpdate(requestAuction.getTitle(), requestAuction.getContent(), requestAuction.getMinPrice(),
			requestAuction.getUserType(), requestAuction.getMaxPrice(), requestAuction.getReservation(),
			requestAuction.getDeadline());
		return auction;
	}

	public boolean deleteAuction(long auctionId) {
		Auction auction = getAuction(auctionId);
		auctionRepository.delete(auction);
		return true;
	}

}
