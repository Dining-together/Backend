package kr.or.dining_together.auction.advice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.gson.JsonObject;

import kr.or.dining_together.auction.advice.exception.BadRequestException;
import kr.or.dining_together.auction.advice.exception.NotCompletedException;
import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.advice.exception.UnAuthorizedException;
import kr.or.dining_together.auction.advice.exception.UnprovenStoreException;
import kr.or.dining_together.auction.advice.exception.UserNotFoundException;
import kr.or.dining_together.auction.advice.exception.UserNotMatchedException;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ExceptionAdvice {

	private final ResponseService responseService;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "실패");
	}

	@ExceptionHandler(ResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected CommonResult resourceNotExistException(HttpServletRequest request, ResourceNotExistException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "요청한 자원이 존재 하지 않습니다.");
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "사용자가 존재하지 않습니다.");
	}

	@ExceptionHandler(UserNotMatchedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, UserNotMatchedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "작성자와 일치하지 않습니다.");
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult badRequestException(HttpServletRequest request, BadRequestException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "요청 자원이 알맞지 않습니다..");
	}

	@ExceptionHandler(UnAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected CommonResult badRequestException(HttpServletRequest request, UnAuthorizedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.UNAUTHORIZED.value(), "권한을 가지고 있지 않습니다.");
	}

	@ExceptionHandler(UnprovenStoreException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult unprovenStoreException(HttpServletRequest request,
		UnprovenStoreException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "업체 인증을 진행해주세요");
	}

	@ExceptionHandler(NotCompletedException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult notCompletedException(HttpServletRequest request,
		NotCompletedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "완료되지 않은 작업입니다.");
	}

	public void exceptionLogCall(String errorMessage){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","info");
		jsonObject.addProperty("target", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		jsonObject.addProperty("content",errorMessage);

		log.info(String.valueOf(jsonObject));
	}
}
