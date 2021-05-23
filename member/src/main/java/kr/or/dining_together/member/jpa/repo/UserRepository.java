package kr.or.dining_together.member.jpa.repo;


import kr.or.dining_together.member.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
