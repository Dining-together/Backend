package kr.or.dining_together.member.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.advice.exception.FileNotFoundException;
import kr.or.dining_together.member.advice.exception.PasswordNotMatchedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.UserIdDto;
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.StorageService;
import kr.or.dining_together.member.service.UserService;
import kr.or.dining_together.member.vo.CustomerProfileRequest;
import kr.or.dining_together.member.vo.CustomerProfileResponse;
import kr.or.dining_together.member.vo.StoreProfileRequest;
import kr.or.dining_together.member.vo.StoreProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: UserController.java
 * @date : 2021/05/30 12:42 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 마이페이지 내 정보
 * @modified :
 **/
@Api(tags = {"3. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
@Slf4j
public class UserController {

	private final static String USER_IMAGE_FOLDER_DIRECTORY = "/user/photo";
	private final static String USER_IMAGE_FILES_POSTFIX = "_photo";

	private final Environment env;
	private final UserService userService;
	private final ResponseService responseService;
	private final UserRepository userRepository;
	private final StorageService storageService;

	@GetMapping("/health_check")
	@Timed(value = "users.status",longTask = true)
	public String status(HttpServletRequest httpServletRequest) {
		return String.format("It's Working in Member Service"
			+ ", port(server.port)=" + env.getProperty("server.port")
			+ ", with token secret=" + env.getProperty("spring.jwt.secret")
			+ ", with token secret=" + env.getProperty("spring.mail.port")
			+ ", with token secret=" + env.getProperty("spring.social.kakao.client_id")
		);
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원 정보 조회", notes = "회원 마이페이지 조회")
	@GetMapping(value = "/customer")
	public SingleResult<Customer> getCustomer() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(userService.getCustomer(email));
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "업체 정보 조회", notes = "업체 마이페이지 조회")
	@GetMapping(value = "/store")
	public SingleResult<Store> getStore() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(userService.getStore(email));
	}

	@ApiOperation(value = "회원 이미지 등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/image")
	@Transactional
	public CommonResult saveFile(
		@RequestBody @ApiParam(value = "회원사진", required = true) MultipartFile file) throws
		Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = (User)userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + USER_IMAGE_FILES_POSTFIX;
		String filePath = storageService.save(file, fileName, USER_IMAGE_FOLDER_DIRECTORY);
		if (filePath == "none") {
			throw new FileNotFoundException();
		} else {//이미지 경로 저장.
			user.imageUpdate(filePath);
		}

		log.info(user.getEmail() + " 이 회원사진을 " + filePath + " 에 등록하였습니다.");
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "회원 정보 수정")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PutMapping(value = "/customer")
	public SingleResult<CustomerProfileResponse> modify(
		@RequestBody @ApiParam(value = "회원가입 정보", required = true) CustomerProfileRequest customerProfileRequest) throws
		Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(userService.modify(customerProfileRequest, email));
	}

	@ApiOperation(value = "업체 정보 수정(회원)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PutMapping(value = "/store")
	public SingleResult<StoreProfileResponse> modify(
		@RequestBody @ApiParam(value = "회원가입 정보", required = true) StoreProfileRequest storeProfileRequest) throws
		Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(userService.modify(storeProfileRequest, email));
	}

	@ApiOperation(value = "회원 탈퇴")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@DeleteMapping(value = "/user")
	public CommonResult delete() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		userService.delete(email);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "비밀번호 확인")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/password/verification")
	public CommonResult verifyPassword(
		@ApiParam(value = "비밀번호 확인", required = true) @RequestParam String password) throws
		Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		if (!userService.isValidPassword(email, password)) {
			throw new PasswordNotMatchedException();

		}
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "회원 정보 조회 (다른 서비스 호출)", notes = "인증된 사용자 조회")
	@GetMapping(value = "/userId")
	public UserIdDto getUserId() throws Throwable {
		log.info("Before retrieve user data");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		log.info("Add retrieved user data");
		return userService.getUserId(email);
	}

}
