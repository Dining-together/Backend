package kr.or.dining_together.auction.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;


import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
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

	public List<Auction> getAuctions(){
		return auctionRepository.findAll();
	}

	public Auction getAuction(long auctionId){
		return auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
	}

	public Auction writeAuction(AuctionDto auctionDto){
		Auction auction=Auction.builder()
			.auctionId(auctionDto.getAuctionId())
			.title(auctionDto.getTitle())
			.content(auctionDto.getContent())
			.max_price(auctionDto.getMax_price())
			.min_price(auctionDto.getMin_price())
			.userType(auctionDto.getUserType())
			.reservation(auctionDto.getReservation())
			.deadline(auctionDto.getDeadline())
			.build();

		return auctionRepository.save(auction);
	}

	public Auction updateAuction(long auctionId,AuctionDto auctionDto){
		Auction auction=getAuction(auctionId);
		auction.setUpdate(auctionDto.getTitle(), auctionDto.getContent(), auctionDto.getMin_price(),
			auctionDto.getUserType(), auction.getMax_price(), auctionDto.getReservation(),auctionDto.getDeadline());
		return auction;
	}

	public boolean deleteAuction(long auctionId){
		Auction auction=getAuction(auctionId);
		auctionRepository.delete(auction);
		return true;
	}


}
