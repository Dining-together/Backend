package kr.or.dining_together.member.service;

import java.util.Collections;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.CustomerRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.KakaoProfile;
import kr.or.dining_together.member.vo.KakaoProfile.Kakao_properties;
import kr.or.dining_together.member.vo.LoginRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final KakaoService kakaoService;
	private final CustomerRepository customerRepository;

	public UserDto login(LoginRequest loginRequest) throws Throwable {
		User user = (User)userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new LoginFailedException();
		}
		return modelMapper.map(user, UserDto.class);
	}

	public void save(UserDto userDto) {
		User user=User.builder()
			.email(userDto.getEmail())
			.password(passwordEncoder.encode(userDto.getPassword()))
			.name(userDto.getName())
			.roles(userDto.getRoles())
			.build();

		userRepository.save(user);

		Long saveResult = user.getId();
		if (saveResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}

	public User signupByKakao(String accessToken, String provider){
		KakaoProfile profile=kakaoService.getKakaoProfile(accessToken);
		KakaoProfile.Kakao_account kakaoAccount = profile.getKakao_account();
		Optional<User> user=userRepository.findByEmailAndProvider(String.valueOf(kakaoAccount.getEmail()),provider);
		if(user.isPresent()){
			return user.get();
		}else{
			User kakaoUser=User.builder()
				.email(String.valueOf(kakaoAccount.getEmail()))
				.name(kakaoAccount.getEmail())
				.provider(provider)
				.roles(Collections.singletonList("ROLE_USER"))
				.build();

			userRepository.save(kakaoUser);
			return kakaoUser;

		}
	}

}
