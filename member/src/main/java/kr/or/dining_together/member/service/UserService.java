package kr.or.dining_together.member.service;

import kr.or.dining_together.member.advice.exception.CEmailloginFailedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.jpa.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(CEmailloginFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailloginFailedException();
        return modelMapper.map(user,UserDto.class);

    }
}
