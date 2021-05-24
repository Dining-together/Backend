package kr.or.dining_together.member.service;

import kr.or.dining_together.member.advice.exception.loginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.vo.RequestLogin;
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

    public UserDto login(RequestLogin requestLogin) {
        User user = userRepository.findByEmail(requestLogin.getEmail()).orElseThrow(loginFailedException::new);
        if (!passwordEncoder.matches(requestLogin.getPassword(), user.getPassword()))
            throw new loginFailedException();
        return modelMapper.map(user,UserDto.class);

    }
}
