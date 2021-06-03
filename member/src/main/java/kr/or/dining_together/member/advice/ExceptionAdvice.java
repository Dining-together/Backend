package kr.or.dining_together.member.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.or.dining_together.member.advice.exception.AuthenticationEntryPointException;
import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.service.ResponseService;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

	private final ResponseService responseService;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "실패");
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "사용자가 존재하지 않습니다.");
	}

	@ExceptionHandler(LoginFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult loginFailedException(HttpServletRequest request, LoginFailedException e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "로그인 실패했습니다.");
	}

	@ExceptionHandler(DataSaveFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userSaveFailedException(HttpServletRequest request, LoginFailedException e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원가입 정보저장에 실패했습니다..");
	}

	@ExceptionHandler(UserDuplicationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult userDuplicationException(HttpServletRequest request, LoginFailedException e) {
		return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "이미 등록된 회원 이메일입니다.");
	}

	@ExceptionHandler(VerificationFailedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult verificationFailedException(HttpServletRequest request, LoginFailedException e) {
		return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "이메일 인증키가 일치하지 않습니다");
	}

	@ExceptionHandler(AuthenticationEntryPointException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonResult authenticationEntryPointException(HttpServletRequest request,
		AuthenticationEntryPointException e) {
		return responseService.getFailResult(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다");
	}
}
