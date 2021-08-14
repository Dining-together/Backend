package kr.or.dining_together.search.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.search.advice.exception.ResourceNotExistException;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.dto.StoreDto;
import kr.or.dining_together.search.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

	private final StoreRepository storeRepository;

	public void createStoreIndex(StoreDto storeDto)
	{
		Store store = Store.builder()
			.id(storeDto.getStoreId())
			.title(storeDto.getStoreName())
			.comment(storeDto.getComment())
			.addr(storeDto.getAddr())
			.storeType(storeDto.getStoreType())
			.openTime(
				LocalDateTime.parse(storeDto.getOpenTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.closedTime(
				LocalDateTime.parse(storeDto.getClosedTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.longitude(Double.toString(storeDto.getLongitude()))
			.latitude(Double.toString(storeDto.getLatitude()))
			.phoneNum(storeDto.getPhoneNum())
			.storeImagePath(storeDto.getStoreImagePath())
			.build();

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
		List<Store> stores = storeRepository.findAllByTitleContaining(title);

		if (stores.isEmpty()) {
			String message = "search result is not exist";
			log.info("error-log :: {}", message);
			throw new ResourceNotExistException();
		}

		return storeRepository.findAllByTitleContaining(title);
	}
}
