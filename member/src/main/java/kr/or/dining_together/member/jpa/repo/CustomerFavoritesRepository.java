package kr.or.dining_together.member.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.jpa.entity.CustomerFavorites;

public interface CustomerFavoritesRepository extends JpaRepository<CustomerFavorites, Long> {
	@Override
	Optional<CustomerFavorites> findById(Long id);

	List<CustomerFavorites> findAllByCustomer(long userId);

	CustomerFavorites findByStoreId(Long storeId);

	@Transactional
	@Modifying
	Long deleteByCustomerAndStoreId(Long customerId, Long storeId);
}
