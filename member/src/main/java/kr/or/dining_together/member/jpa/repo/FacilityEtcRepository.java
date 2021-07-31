package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.FacilityEtc;

public interface FacilityEtcRepository extends JpaRepository<FacilityEtc, Long> {

	@Transactional
	@Modifying
	@Query("delete from FacilityEtc c where c.facility in :facility")
	void deleteAllByFacility(@Param("facility") Facility facility);

}
