package kr.or.dining_together.auction.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

	List<Auction> findAllByUserId(long userId);

	List<Auction> findAllByStatus(AuctionStatus status);

	// @Modifying
	// @Query("update Auction a set a.isDeadline=false where a.deadline < :date")
	// void updateAuctionDeadlineEnd(@Param("date") Date date);

}
