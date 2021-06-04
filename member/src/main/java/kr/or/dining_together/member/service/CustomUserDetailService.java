package kr.or.dining_together.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) {
		//오류 있어서 수정햇는데 잘되는지 확인 필요
		User user = null;
		try {
			user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return user;
	}
}
