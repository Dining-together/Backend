package kr.or.dining_together.auction.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}