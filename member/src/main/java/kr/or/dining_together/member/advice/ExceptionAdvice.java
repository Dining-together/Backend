package kr.or.dining_together.member.advice;

import kr.or.dining_together.member.advice.exception.AuthenticationEntryPointException;
import kr.or.dining_together.member.advice.exception.loginFailedException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request,Exception e){
        return responseService.getFailResult(500,"실패");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFound(HttpServletRequest request, UserNotFoundException e) {
        return responseService.getFailResult(501,"사용자가 존재하지 않습니다.");
    }

    @ExceptionHandler(loginFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailloginFailed(HttpServletRequest request, loginFailedException e) {
        return responseService.getFailResult(502,"로그인 실패했습니다..");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, AuthenticationEntryPointException e) {
        return responseService.getFailResult(401, "권한이 없습니다");
    }


}
