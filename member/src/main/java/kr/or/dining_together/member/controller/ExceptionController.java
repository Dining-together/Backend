package kr.or.dining_together.member.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.or.dining_together.member.advice.exception.AuthenticationEntryPointException;
import kr.or.dining_together.member.model.CommonResult;
import lombok.RequiredArgsConstructor;

@Api(tags = {"2. Exception"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member/exception")
public class ExceptionController {

	@GetMapping(value = "/entrypoint")
	public CommonResult entrypointException() {
		throw new AuthenticationEntryPointException();
	}

	@ApiOperation(value = "인가거부 에러", notes = "접근 불가능한 url에 접근시에 보내는 에러")
	@GetMapping(value = "/accessdenied")
	public CommonResult accessdeniedException() {
		throw new AccessDeniedException("");
	}
}