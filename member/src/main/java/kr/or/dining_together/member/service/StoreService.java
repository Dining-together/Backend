package kr.or.dining_together.member.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UnprovenStoreException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.FacilityEtc;
import kr.or.dining_together.member.jpa.entity.FacilityFacilityEtc;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.FacilityEtcRepository;
import kr.or.dining_together.member.jpa.repo.FacilityFacilityEtcRepository;
import kr.or.dining_together.member.jpa.repo.FacilityRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.vo.FacilityRequest;
import kr.or.dining_together.member.vo.StoreRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final FacilityRepository facilityRepository;
	private final FacilityFacilityEtcRepository facilityFacilityEtcRepository;
	private final FacilityEtcRepository facilityEtcRepository;

	public Store getStore(long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(UserNotFoundException::new);
		return store;
	}

	public List<Store> getStores() {
		return storeRepository.findAll();
	}

	public Store registerStore(StoreRequest storeRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (store.getDocumentChecked() == false) {
			throw new UnprovenStoreException();
		}
		store.update(storeRequest.getPhoneNum(), storeRequest.getAddr(), storeRequest.getLatitude(),
			storeRequest.getLongitude());
		storeRepository.save(store);
		return store;
	}

	@Transactional
	public Facility registerFacility(FacilityRequest facilityRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (store.getDocumentChecked() == false) {
			throw new UnprovenStoreException();
		}
		Facility facility = Facility.builder()
			.capacity(facilityRequest.getCapacity())
			.parkingCount(facilityRequest.getParkingCount())
			.parking(facilityRequest.isParking())
			.openTime(facilityRequest.getOpenTime())
			.closedTime(facilityRequest.getClosedTime())
			.build();

		facility = facilityRepository.save(facility);
		List<String> names = facilityRequest.getFacilityEtcNames();
		for (String name : names) {
			FacilityEtc facilityEtc = facilityEtcRepository.findByName(name)
				.orElseThrow(ResourceNotExistException::new);
			FacilityFacilityEtc facilityFacilityEtc = FacilityFacilityEtc.builder()
				.facilityEtc(facilityEtc)
				.facility(facility)
				.build();

			facilityFacilityEtcRepository.save(facilityFacilityEtc);
		}
		store.setFacility(facility);
		storeRepository.save(store);
		return facility;
	}

	@Transactional
	public Facility modifyFacility(FacilityRequest facilityRequest, long facilityId, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		Facility facility = facilityRepository.findById(facilityId).orElseThrow(ResourceNotExistException::new);
		facilityFacilityEtcRepository.deleteAllByFacility(facility);
		List<String> names = facilityRequest.getFacilityEtcNames();
		for (String name : names) {
			FacilityEtc facilityEtc = facilityEtcRepository.findByName(name)
				.orElseThrow(ResourceNotExistException::new);
			FacilityFacilityEtc facilityFacilityEtc = FacilityFacilityEtc.builder()
				.facilityEtc(facilityEtc)
				.facility(facility)
				.build();

			facilityFacilityEtcRepository.save(facilityFacilityEtc);
		}
		facility.update(facilityRequest.getCapacity(), facilityRequest.getParkingCount(), facilityRequest.isParking(),
			facilityRequest.getOpenTime(), facilityRequest.getClosedTime());
		facilityRepository.save(facility);
		return facility;
	}

}
