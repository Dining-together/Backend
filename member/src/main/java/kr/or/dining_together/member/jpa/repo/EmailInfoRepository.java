package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.EmailInfo;

public interface EmailInfoRepository extends JpaRepository<EmailInfo, Long> {

	Optional<EmailInfo> findByEmail(String email);

	Optional<EmailInfo> findByKey(String key);
}