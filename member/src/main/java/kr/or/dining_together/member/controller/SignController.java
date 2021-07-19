package kr.or.dining_together.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.config.security.JwtTokenProvider;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.EmailService;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.UserService;
import kr.or.dining_together.member.vo.LoginRequest;
import kr.or.dining_together.member.vo.SignUpRequest;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: SignController.java
 * @date : 2021/05/30 12:43 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 로그인 회원가입
 * @modified :
 **/
@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member/auth")
public class SignController {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final EmailService emailService;

	@ApiOperation(value = "로그인", notes = "이메일을 통해 로그인한다.")
	@PostMapping(value = "/signin")
	public SingleResult<String> userlogin(
		@RequestBody @ApiParam(value = "이메일 비밀번호", required = true) LoginRequest loginRequest, HttpServletRequest req,
		HttpServletResponse res) throws Throwable {
		UserDto userDto = userService.login(loginRequest);
		return responseService.getSingleResult(
			jwtTokenProvider.createToken(String.valueOf(userDto.getEmail()), userDto.getRoles()));
	}

	@ApiOperation(value = "회원가입", notes = "CustomerSignUpRequest 객체를 입력 받아 회원가입 한다.")
	@PostMapping(value = "/signup")
	public CommonResult userSignUp(
		@RequestBody @ApiParam(value = "회원가입 정보", required = true) SignUpRequest signUpRequest) {
		userService.save(signUpRequest);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "이메일 중복확인", notes = "이메일 string을 입력받아 존재하는 email인지 확인한다.")
	@GetMapping(value = "/signup")
	public CommonResult userSignUpCheckEmail(
		@RequestParam @ApiParam(value = "등록하려는 이메일", required = true) String email) {
		emailService.checkEmailExistence(email);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "이메일 인증 요청", notes = "이메일에 키값을 보낸다.")
	@PostMapping(value = "/verify")
	public CommonResult verify(
		@RequestParam @ApiParam(value = "인증하려는 이메일", required = true) String email) {
		emailService.sendVerificationMail(email);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "이메일 키값 인증", notes = "이메일과 키값을 받아 맞는지 확인한다.")
	@GetMapping(value = "/verify")
	public CommonResult getVerify(
		@RequestParam @ApiParam(value = "이메일 정보", required = true) String email,
		@RequestParam @ApiParam(value = "키값 정보", required = true) String key) {
		emailService.verifyEmail(email, key);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "인증 및 비밀번호 전송", notes = "이메일과 키값을 받아 맞는지 확인하고 맞으면 비밀번호를 전송한다.")
	@GetMapping(value = "/verify/password")
	public CommonResult getVerifyAndGetPassword(
		@RequestParam @ApiParam(value = "이메일 정보", required = true) String email,
		@RequestParam @ApiParam(value = "키값 정보", required = true) String key) {
		emailService.verifyEmail(email, key);
		emailService.sendUserPassword(email);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "소셜 로그인", notes = " 소셜 회원 로그인을 한다.")
	@PostMapping(value = "/signin/{provider}")
	public SingleResult<String> signinByProvider(
		@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
		@ApiParam(value = "소셜 access token", required = true) @RequestParam String accessToken) {

		User signedUser = null;

		switch (provider) {
			case "naver":
				signedUser = userService.signupByNaver(accessToken, provider);
				break;
			case "kakao":
				signedUser = userService.signupByKakao(accessToken, provider);
				break;
		}

		return responseService.getSingleResult(
			jwtTokenProvider.createToken(String.valueOf(signedUser.getEmail()), signedUser.getRoles()));

	}
}
