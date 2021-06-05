package kr.or.dining_together.auction.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
