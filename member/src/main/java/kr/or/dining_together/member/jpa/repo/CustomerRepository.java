package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
