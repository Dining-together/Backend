package kr.or.dining_together.member.advice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.gson.JsonObject;

import kr.or.dining_together.member.advice.exception.AuthenticationEntryPointException;
import kr.or.dining_together.member.advice.exception.ComunicationException;
import kr.or.dining_together.member.advice.exception.DataSaveFailedException;
import kr.or.dining_together.member.advice.exception.FileNotFoundException;
import kr.or.dining_together.member.advice.exception.LoginFailedException;
import kr.or.dining_together.member.advice.exception.PasswordNotMatchedException;
import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UnprovenStoreException;
import kr.or.dining_together.member.advice.exception.UserDuplicationException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.advice.exception.VerificationFailedException;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "사용자가 존재하지 않습니다.");
	}

	@ExceptionHandler(LoginFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult loginFailedException(HttpServletRequest request, LoginFailedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "로그인 실패했습니다.");
	}

	@ExceptionHandler(DataSaveFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userSaveFailedException(HttpServletRequest request, DataSaveFailedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원가입 정보저장에 실패했습니다..");
	}

	@ExceptionHandler(UserDuplicationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult userDuplicationException(HttpServletRequest request, UserDuplicationException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 등록된 회원 이메일입니다.");
	}

	@ExceptionHandler(VerificationFailedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult verificationFailedException(HttpServletRequest request, VerificationFailedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "이메일 인증키가 일치하지 않습니다");
	}

	@ExceptionHandler(AuthenticationEntryPointException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonResult authenticationEntryPointException(HttpServletRequest request,
		AuthenticationEntryPointException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다");
	}

	@ExceptionHandler(ComunicationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult comunicationException(HttpServletRequest request, ComunicationException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "통신 중 오류가 발생했습니다.");
	}

	@ExceptionHandler(PasswordNotMatchedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult passwordNotMatchedException(HttpServletRequest request,
		PasswordNotMatchedException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "패스워드가 맞지 않습니다.");
	}

	@ExceptionHandler(ResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult resourceNotExistException(HttpServletRequest request,
		ResourceNotExistException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "요청한 자원이 존재하지 않습니다.");
	}

	@ExceptionHandler(FileNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult fileNotFoundException(HttpServletRequest request,
		FileNotFoundException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "파일이 존재하지 않습니다.");
	}

	@ExceptionHandler(UnprovenStoreException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResult unprovenStoreException(HttpServletRequest request,
		UnprovenStoreException e) {
		exceptionLogCall(e.toString());

		return responseService.getFailResult(HttpStatus.NOT_FOUND.value(), "업체 인증을 진행해주세요");
	}

	public void exceptionLogCall(String errorMessage){
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","error");
		jsonObject.addProperty("target", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		jsonObject.addProperty("content",errorMessage);

		log.info(String.valueOf(jsonObject));
	}

}
