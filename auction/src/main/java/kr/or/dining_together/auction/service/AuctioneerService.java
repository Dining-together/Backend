package kr.or.dining_together.auction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.dto.AuctioneerDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.jpa.repo.AuctioneerRepository;
import kr.or.dining_together.auction.vo.AuctioneerRequest;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.auction.service
 * @name: AuctioneerService.java
 * @date : 2021/06/16 2:12 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class AuctioneerService {

	private final AuctioneerRepository auctioneerRepository;
	private final AuctionRepository auctionRepository;

	public List<Auctioneer> getAuctioneers(long auctionId) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);
		List<Auctioneer> auctioneers = auctioneerRepository.findAuctioneersByAuction(auction);
		return auctioneers;
	}

	public AuctioneerDto registerAuctioneer(AuctioneerRequest auctioneerRequest, UserIdDto userIdDto, long auctionId) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotExistException::new);

		Auctioneer auctioneer = Auctioneer.builder()
			.content(auctioneerRequest.getContent())
			.auction(auction)
			.price(auctioneerRequest.getPrice())
			.menu(auctioneerRequest.getMenu())
			.storeId(userIdDto.getId())
			.storeName(userIdDto.getName())
			.build();

		auctioneerRepository.save(auctioneer);

		return new ModelMapper().map(auctioneer, AuctioneerDto.class);
	}

	public AuctioneerDto modifyAuctioneer(AuctioneerRequest auctioneerRequest, long auctioneerId) {

		Auctioneer auctioneer = auctioneerRepository.findById(auctioneerId).orElseThrow(ResourceNotExistException::new);

		auctioneer.update(auctioneerRequest.getContent(), auctioneer.getMenu(), auctioneerRequest.getPrice());
		return new ModelMapper().map(auctioneer, AuctioneerDto.class);
	}

	public boolean deleteAuctioneer(long auctioneerId) {

		Auctioneer auctioneer = auctioneerRepository.findById(auctioneerId).orElseThrow(ResourceNotExistException::new);

		auctioneerRepository.delete(auctioneer);
		return true;
	}

}
