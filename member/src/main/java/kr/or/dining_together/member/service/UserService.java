package kr.or.dining_together.member.service;

import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.vo.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(LoginFailedException::new);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new LoginFailedException();
        return modelMapper.map(user,UserDto.class);

    }
}
