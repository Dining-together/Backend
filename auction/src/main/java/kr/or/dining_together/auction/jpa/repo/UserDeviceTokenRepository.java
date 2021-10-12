package kr.or.dining_together.auction.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.auction.jpa.entity.UserDeviceToken;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
	UserDeviceToken getUserDeviceTokenByEmail(String email);
}
