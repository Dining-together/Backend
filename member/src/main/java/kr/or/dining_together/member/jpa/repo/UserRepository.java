package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Override
	Optional<User> findById(Long id);

	Optional<User> findByEmail(String email);
}
