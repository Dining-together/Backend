package kr.or.dining_together.member.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.vo.LoginRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private ModelMapper modelMapper;

	public UserDto login(LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
			throw new LoginFailedException();
		return modelMapper.map(user, UserDto.class);
	}

	public Long save(UserDto userDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userDto.setPassword(encoder.encode(userDto.getPassword()));

		return userRepository.save(User.builder()
			.email(userDto.getEmail())
			.password(userDto.getPassword())
			.name(userDto.getName())
			.phoneNo(userDto.getPhoneNo())
			.roles(userDto.getRoles())
			.build()).getId();
	}

}
