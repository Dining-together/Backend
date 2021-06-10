package kr.or.dining_together.member.service;

import java.util.Collections;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.SignUserDto;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.CustomerRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.KakaoProfile;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.NaverProfile;
import kr.or.dining_together.member.vo.SignUpRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final KakaoService kakaoService;
	private final NaverService naverService;
	private final CustomerRepository customerRepository;
	private final StoreRepository storeRepository;

	public Long getUserId(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}
		return user.get().getId();
	}

	public UserDto login(LoginRequest loginRequest) throws Throwable {
		User user = (User)userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new LoginFailedException();
		}
		return modelMapper.map(user, UserDto.class);
	}

	@Transactional
	public void save(SignUpRequest signUpRequest) {
		UserType userType = signUpRequest.getUserType();
		SignUserDto userDto = signUpRequest.getSignUserDto();

		if (userType == UserType.CUSTOMER) {
			userRepository.save(Customer.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.name(userDto.getName())
				.gender(signUpRequest.getGender())
				.age(signUpRequest.getAge())
				.provider("application")
				.build());
		} else if (userType == UserType.STORE) {
			userRepository.save(Store.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.name(userDto.getName())
				.documentChecked(false)
				.provider("application")
				.build());
		}

		if (userRepository.findByEmail(signUpRequest.getSignUserDto().getEmail()).isEmpty()) {
			throw new DataSaveFailedException();
		}
	}

	public User signupByKakao(String accessToken, String provider) {
		KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
		KakaoProfile.Kakao_account kakaoAccount = profile.getKakao_account();
		Optional<User> user = userRepository.findByEmailAndProvider(String.valueOf(kakaoAccount.getEmail()),
			provider);
		if (user.isPresent()) {
			return user.get();
		} else {
			Customer kakaoUser = Customer.builder()
				.email(String.valueOf(kakaoAccount.getEmail()))
				.name(kakaoAccount.getEmail())
				.provider(provider)
				.roles(Collections.singletonList("ROLE_USER"))
				.build();

			userRepository.save(kakaoUser);
			return kakaoUser;
		}
	}

	public User signupByNaver(String provider, String accessToken) {
		NaverProfile naverProfile = naverService.getNaverProfile(accessToken);
		NaverProfile.Response naverAccount = naverProfile.getResponse();
		Optional<User> user = userRepository.findByEmailAndProvider(naverAccount.getEmail(), provider);
		if (user.isPresent()) {
			return user.get();
		} else {
			Customer naverUser = Customer.builder()
				.email(naverAccount.getEmail())
				.name(naverAccount.getName())
				.provider(provider)
				.roles(Collections.singletonList("ROLE_USER"))
				.build();

			userRepository.save(naverUser);

			return naverUser;
		}
	}

}
