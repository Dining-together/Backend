package kr.or.dining_together.member.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.entity.UserType;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.SignUpRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public UserDto login(LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new LoginFailedException();
		}
		return modelMapper.map(user, UserDto.class);
	}

	@SuppressWarnings("checkstyle:RegexpSingleline")
	public void save(SignUpRequest signUpRequest) {
		String userType = signUpRequest.getUserType().getValue();
		UserDto userDto = signUpRequest.getUserDto();
		Long saveResult = null;

		if (userType == UserType.CUSTOMER.getValue()) {
			saveResult = userRepository.save(Customer.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.name(userDto.getName())
				.roles(userDto.getRoles())
				.gender(signUpRequest.getGender())
				.dateOfBirth(signUpRequest.getDateOfBirth())
				.phoneNo(signUpRequest.getPhoneNo())
				.build()).getId();
		} else if (userType == UserType.STORE.getValue()) {
			saveResult = userRepository.save(Store.builder()
				.email(userDto.getEmail())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.name(userDto.getName())
				.roles(userDto.getRoles())
				.documentChecked(false)
				.build()).getId();
		}

		if (saveResult == null) {
			throw new DataSaveFailedException();
		}
		return;
	}

}
