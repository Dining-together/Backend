package kr.or.dining_together.search.service;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private static final String STORE_INDEX = "stores";

	private final StoreRepository storeRepository;
	private final RestHighLevelClient elasticsearchClient;

	public void createStoreIndex(Store store) {
		storeRepository.save(store);
	}

	// public void createStoreIndexBulk(List<Store> stores) {
	// 	storeRepository.saveAll(stores);
	// }

	// public void deleteStoreDocument(String id) throws IOException {
	// 	storeRepository.deleteById(id);
	// }

	public List<Store> findByTitle(final String title) {
		return storeRepository.findAllByTitle(title);
	}

	public List<Store> findByTitleMatchingNames(final String title) {
		return storeRepository.findAllByTitleContaining(title);
	}
}
