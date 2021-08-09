package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.Store;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

	@Transactional
	@Modifying
	@Query("delete from Facility c where c.store=:store")
	void deleteAllByStore(Store store);

}
