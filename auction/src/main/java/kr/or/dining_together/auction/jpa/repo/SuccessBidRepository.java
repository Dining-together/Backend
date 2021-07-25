package kr.or.dining_together.auction.jpa.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.or.dining_together.auction.jpa.entity.SuccessBid;

public interface SuccessBidRepository extends JpaRepository<SuccessBid, Long> {

	List<SuccessBid> findAllByUserId(long userId);

	@Query("select a from SuccessBid a where a.isComplete=false and a.reservation < :date")
	List<SuccessBid> findAllByCompleteFalseAndReservationAfter(LocalDateTime date);

}
