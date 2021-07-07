package kr.or.dining_together.member.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByEmail(String email);

}
