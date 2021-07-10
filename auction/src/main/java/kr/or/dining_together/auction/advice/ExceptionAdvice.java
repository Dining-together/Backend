package kr.or.dining_together.auction.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.or.dining_together.auction.advice.exception.BadRequestException;
import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.advice.exception.UnAuthorizedException;
import kr.or.dining_together.auction.advice.exception.UserNotFoundException;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.service.ResponseService;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.auction.advice
 * @name: ExceptionAdvice.java
 * @date : 2021/06/05 9:59 오후
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 결과 처리
 * @modified :
 **/
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

	private final ResponseService responseService;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "실패");
	}

	@ExceptionHandler(ResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected CommonResult resourceNotExistException(HttpServletRequest request, ResourceNotExistException e) {
		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "요청한 자원이 존재 하지 않습니다.");
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "사용자가 존재하지 않습니다.");
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult badRequestException(HttpServletRequest request, BadRequestException e) {
		return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "요청 자원이 알맞지 않습니다..");
	}

	@ExceptionHandler(UnAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected CommonResult badRequestException(HttpServletRequest request, UnAuthorizedException e) {
		return responseService.getFailResult(HttpStatus.UNAUTHORIZED.value(), "권한을 가지고 있지 않습니다.");
	}
}
