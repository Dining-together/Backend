package kr.or.dining_together.auction.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.SuccessBid;

public interface SuccessBidRepository extends JpaRepository<SuccessBid, Long> {

	List<SuccessBid> findAllByUserId(long userId);

}
