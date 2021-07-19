package kr.or.dining_together.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import kr.or.dining_together.search.document.Auction;

@Repository("auctionRepository")
public interface AuctionRepository extends ElasticsearchRepository<Auction, String> {
	List<Auction> findAllByTitle(String title);

	List<Auction> findAllByTitleContaining(String title);

}
