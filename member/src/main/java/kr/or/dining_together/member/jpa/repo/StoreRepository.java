package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
