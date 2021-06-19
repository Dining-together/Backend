package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.dining_together.member.jpa.entity.User;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

	@Override
	Optional<T> findById(Long id);

	Optional<T> findByEmail(String email);

	Optional<T> findByEmailAndProvider(String email, String provider);

	@Modifying
	@Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
	void updatePassword(@Param("password") String password, @Param("email") String email);
}
