package kr.or.dining_together.auction.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;

public interface AuctioneerRepository extends JpaRepository<Auctioneer, Long> {

	List<Auctioneer> findAuctioneersByAuction(Auction auction);

	List<Auctioneer> findAllByAuctionOrderByUpdatedDateDesc(Auction auction);

	List<Auctioneer> findAuctioneersByStoreId(long storeId);

}
