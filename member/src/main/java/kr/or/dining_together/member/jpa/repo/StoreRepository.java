package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.or.dining_together.member.jpa.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByEmail(String email);

}
