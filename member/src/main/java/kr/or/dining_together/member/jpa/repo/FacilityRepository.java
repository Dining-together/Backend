package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

}
