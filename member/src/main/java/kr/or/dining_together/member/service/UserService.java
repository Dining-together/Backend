package kr.or.dining_together.member.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.advice.exception.PasswordNotMatchedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.dto.UserIdDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.CustomerRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.CustomerProfileRequest;
import kr.or.dining_together.member.vo.CustomerProfileResponse;
import kr.or.dining_together.member.vo.KakaoProfile;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.NaverProfile;
import kr.or.dining_together.member.vo.SignUpRequest;
import kr.or.dining_together.member.vo.StoreProfileRequest;
import kr.or.dining_together.member.vo.StoreProfileResponse;
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

		if (userType == UserType.CUSTOMER) {
			userRepository.save(Customer.builder()
				.email(signUpRequest.getEmail())
				.password(passwordEncoder.encode(signUpRequest.getPassword()))
				.name(signUpRequest.getName())
				.gender(signUpRequest.getGender())
				.latitude(signUpRequest.getLatitude())
				.longitude(signUpRequest.getLongitude())
				.addr(signUpRequest.getAddr())
				.age(signUpRequest.getAge())
				.type("CUSTOMER")
				.build());
		} else if (userType == UserType.STORE) {
			userRepository.save(Store.builder()
				.email(signUpRequest.getEmail())
				.password(passwordEncoder.encode(signUpRequest.getPassword()))
				.name(signUpRequest.getName())
				.documentChecked(false)
				.type("STORE")
				.build());
		}

		if (userRepository.findByEmail(signUpRequest.getEmail()).isEmpty()) {
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
				.build();

			userRepository.save(naverUser);

			return naverUser;
		}
	}

	public UserIdDto getUserId(String email) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		UserIdDto userIdDto=UserIdDto.builder()
			.path(user.getPath())
			.id(user.getId())
			.type(user.getType())
			.name(user.getName())
			.build();
		return userIdDto;
	}

	public Customer getCustomer(String email) throws Throwable {
		Customer user = (Customer)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return user;

	}

	public Store getStore(String email) throws Throwable {
		Store user = (Store)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return user;

	}

	public void delete(String email) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		userRepository.deleteById(user.getId());

	}

	public boolean isValidPassword(String email, String password) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new PasswordNotMatchedException();
		}
		return true;

	}

	// public void updatePassword(String email, String newPassword) throws Throwable {
	// 	User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	// 	user.updatePassword(passwordEncoder.encode(newPassword));
	// }

	public CustomerProfileResponse modify(CustomerProfileRequest customerProfileRequest, String email) throws
		Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		Customer customer = (Customer)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		user.update(passwordEncoder.encode(customerProfileRequest.getPassword()), customerProfileRequest.getName());
		customer.update(customerProfileRequest.getAge(), customerProfileRequest.getGender(),
			customerProfileRequest.getAddr(), customerProfileRequest.getLatitude(),
			customerProfileRequest.getLongitude());

		userRepository.save(user);
		customerRepository.save(customer);

		CustomerProfileResponse customerProfileResponse = CustomerProfileResponse.builder()
			.email(email)
			.age(customer.getAge())
			.gender(customer.getGender())
			.name(user.getName())
			.addr(customer.getAddr())
			.build();

		return customerProfileResponse;
	}

	public StoreProfileResponse modify(StoreProfileRequest storeProfileRequest, String email) throws Throwable {
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		user.update(passwordEncoder.encode(storeProfileRequest.getPassword()), storeProfileRequest.getName());
		userRepository.save(user);
		StoreProfileResponse storeProfileResponse = StoreProfileResponse.builder()
			.email(email)
			.name(user.getName())
			.build();

		return storeProfileResponse;
	}

}
