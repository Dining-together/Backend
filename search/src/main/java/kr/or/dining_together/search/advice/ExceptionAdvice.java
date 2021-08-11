package kr.or.dining_together.search.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.or.dining_together.search.advice.exception.ResourceNotExistException;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.service.ResponseService;
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

	@ExceptionHandler(ResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult resourceNotExistException(HttpServletRequest request,
		ResourceNotExistException e) {
		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "요청한 자원이 존재하지 않습니다.");
	}
}
