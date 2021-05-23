package kr.or.dining_together.member.advice;

import kr.or.dining_together.member.advice.exception.CEmailloginFailedException;
import kr.or.dining_together.member.advice.exception.CUserNotFoundException;
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

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(501,"사용자가 존재하지 않습니다.");
    }

    @ExceptionHandler(CEmailloginFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFound(HttpServletRequest request, CEmailloginFailedException e) {
        return responseService.getFailResult(502,"이메일을 확인해주세요.");
    }


}
