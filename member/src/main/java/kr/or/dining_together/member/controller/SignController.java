package kr.or.dining_together.member.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.config.security.JwtTokenProvider;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.UserService;
import kr.or.dining_together.member.vo.LoginRequest;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class SignController {
	/*
	로그인 회원가입 로직
	 */
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;

	@ApiOperation(value = "로그인", notes = "이메일을 통해 로그인한다.")
	@PostMapping(value = "/user")
	public SingleResult<String> userlogin(
		@RequestBody @ApiParam(value = "이메일 비밀번호", required = true) LoginRequest loginRequest) {
		UserDto userDto = userService.login(loginRequest);
		return responseService.getSingleResult(
			jwtTokenProvider.createToken(String.valueOf(userDto.getEmail()), userDto.getRoles()));
	}

	@ApiOperation(value = "회원가입", notes = "UserDto 객체를 입력 받아 회원가입 한다.")
	@PostMapping(value = "/user/registeration")
	public void userSignUp(@RequestBody @ApiParam(value = "회원가입 정보", required = true) UserDto userDto) {
		userService.save(userDto);
		return;
	}

}
