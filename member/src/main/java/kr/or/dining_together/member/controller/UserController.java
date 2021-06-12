package kr.or.dining_together.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.advice.exception.PasswordNotMatchedException;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: UserController.java
 * @date : 2021/05/30 12:42 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 마이페이지 내 정보
 * @modified :
 **/
@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class UserController {

	private final UserService userService;
	private final ResponseService responseService;

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@ApiOperation(value = "회원 정보 조회", notes = "회원 단건 조회")
	@GetMapping(value = "/user")
	public SingleResult<UserDto> findUser() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(userService.getUser(email));
	}
	//
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	// })
	// @ApiOperation(value = "업체 정보 조회", notes = "업체 단건 조회")
	// @GetMapping(value = "/store")
	// public SingleResult<UserDto> findUser() {
	// 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// 	String email = authentication.getName();
	//
	// 	return responseService.getSingleResult(userService.getUser(email));
	// }

	// @ApiOperation(value="회원 정보 수정")
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	// })
	// @PutMapping(value="/user")
	// public SingleResult<UserDto> modify(@RequestBody UserDto userDto){
	// 	Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	// 	String email=authentication.getName();
	// 	return responseService.getSingleResult(userService.modify(userDto,email));
	// }
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

	@ApiOperation(value = "비밀번호 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PutMapping(value = "/password")
	public CommonResult changePassword(
		@ApiParam(value = "새로운 비밀번호", required = true) @RequestParam String newPassword) throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		userService.updatePassword(email, newPassword);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "비밀번호 변경")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/password/verification")
	public CommonResult verifyPassword(@ApiParam(value = "비밀번호 확인", required = true) @RequestParam String password) throws
		Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		if (!userService.isValidPassword(email, password)) {
			throw new PasswordNotMatchedException();

		}
		return responseService.getSuccessResult();
	}


}
