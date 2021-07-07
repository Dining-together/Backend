package kr.or.dining_together.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import kr.or.dining_together.search.document.Store;

@Repository("storeRepository")
public interface StoreRepository extends ElasticsearchRepository<Store, String> {
	List<Store> findAllByTitle(String title);

	List<Store> findAllByTitleContaining(String title);
}
