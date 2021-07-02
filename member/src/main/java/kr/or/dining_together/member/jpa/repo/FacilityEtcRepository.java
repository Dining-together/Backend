package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.FacilityEtc;

public interface FacilityEtcRepository extends JpaRepository<FacilityEtc, Long> {

	Optional<FacilityEtc> findByName(String name);

}
