package kr.or.dining_together.member.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserDeviceToken;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
	UserDeviceToken getUserDeviceTokenByUser(User user);
}
