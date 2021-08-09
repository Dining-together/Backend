package kr.or.dining_together.member.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UnprovenStoreException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.FacilityEtc;
import kr.or.dining_together.member.jpa.entity.FacilityType;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.FacilityEtcRepository;
import kr.or.dining_together.member.jpa.repo.FacilityRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.vo.FacilityRequest;
import kr.or.dining_together.member.vo.StoreListResponse;
import kr.or.dining_together.member.vo.StoreRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final FacilityRepository facilityRepository;
	private final FacilityEtcRepository facilityEtcRepository;

	public Store getStore(long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(UserNotFoundException::new);
		return store;
	}

	public List<StoreListResponse> getStores() {
		ModelMapper modelMapper = new ModelMapper();
		List<Store> stores = storeRepository.findAll();
		List<StoreListResponse> collect =
			stores.stream().map(p -> modelMapper.map(p, StoreListResponse.class)).collect(Collectors.toList());
		return collect;
	}

	public Store registerStore(StoreRequest storeRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (store.getDocumentChecked() == false) {
			throw new UnprovenStoreException();
		}
		store.update(storeRequest.getPhoneNum(), storeRequest.getAddr(), storeRequest.getLatitude(),
			storeRequest.getLongitude(), storeRequest.getComment(), storeRequest.getStoreType(),
			storeRequest.getOpenTime(),
			storeRequest.getClosedTime());
		storeRepository.save(store);
		return store;
	}

	@Transactional
	public Facility registerFacility(FacilityRequest facilityRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (store.getDocumentChecked() == false) {
			throw new UnprovenStoreException();
		}
		if (store.getFacility() != null) {
			facilityRepository.deleteAllByStore(store);
		}
		Facility facility = Facility.builder()
			.capacity(facilityRequest.getCapacity())
			.parkingCount(facilityRequest.getParkingCount())
			.parking(facilityRequest.isParking())
			.build();

		facility = facilityRepository.save(facility);
		if (facility.getFacilityEtcs() != null) {
			facilityEtcRepository.deleteAllByFacility(facility);
		}
		List<FacilityType> facilityTypes = facilityRequest.getFacilityTypes();
		for (FacilityType type : facilityTypes) {
			FacilityEtc facilityEtc = FacilityEtc.builder()
				.facilityType(type)
				.facility(facility)
				.build();

			facilityEtcRepository.save(facilityEtc);
		}
		store.setFacility(facility);
		storeRepository.save(store);
		return facility;
	}

	@Transactional
	public Facility modifyFacility(FacilityRequest facilityRequest, long facilityId, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		Facility facility = facilityRepository.findById(facilityId).orElseThrow(ResourceNotExistException::new);
		facilityEtcRepository.deleteAllByFacility(facility);
		List<FacilityType> facilityTypes = facilityRequest.getFacilityTypes();
		for (FacilityType type : facilityTypes) {
			FacilityEtc facilityEtc = FacilityEtc.builder()
				.facilityType(type)
				.facility(facility)
				.build();

			facilityEtcRepository.save(facilityEtc);
		}
		facility.update(facilityRequest.getCapacity(), facilityRequest.getParkingCount(), facilityRequest.isParking());
		facilityRepository.save(facility);
		return facility;
	}

}
