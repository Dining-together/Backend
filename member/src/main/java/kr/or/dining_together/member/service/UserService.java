package kr.or.dining_together.member.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.advice.exception.PasswordNotMatchedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
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
	private final ModelMapper modelMapper;

	public UserDto login(LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
			throw new LoginFailedException();
		return modelMapper.map(user, UserDto.class);
	}

	public Long save(UserDto userDto) {
		return userRepository.save(User.builder()
			.email(userDto.getEmail())
			.password(passwordEncoder.encode(userDto.getPassword()))
			.name(userDto.getName())
			.phoneNo(userDto.getPhoneNo())
			.roles(userDto.getRoles())
			.build()).getId();
	}

	public UserDto getUser(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);

	}

	public void delete(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		userRepository.deleteById(user.getId());

	}

	public boolean isValidPassword(String email, String password) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new PasswordNotMatchedException();
		}
		return true;

	}

	public void updatePassword(String email, String newPassword) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		user.updatePassword(passwordEncoder.encode(newPassword));
	}

	// public UserDto modify(UserDto userDto,String email) {
	// 	User user=userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	//
	// }

}
