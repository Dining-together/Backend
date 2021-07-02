package kr.or.dining_together.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
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

	public Store getStore(String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return store;
	}

	public Store registerStore(StoreRequest storeRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		store.update(storeRequest.getPhoneNum(), store.getAddr(), storeRequest.getStoreName());
		return store;
	}

	public Facility registerFacility(FacilityRequest facilityRequest, String email) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

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
