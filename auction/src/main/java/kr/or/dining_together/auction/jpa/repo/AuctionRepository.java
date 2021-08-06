package kr.or.dining_together.auction.jpa.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

	List<Auction> findAll(Sort sort);

	List<Auction> findAllByUserId(long userId);

	List<Auction> findAllByStatus(AuctionStatus status);

	@Query("select a from Auction a where a.status=:status and a.deadline < :date")
	List<Auction> findAllByStatusAndDeadlineAfter(AuctionStatus status, LocalDateTime date);

	// @Modifying
	// @Query("update Auction a set a.isDeadline=false where a.deadline < :date")
	// void updateAuctionDeadlineEnd(@Param("date") Date date);

}
