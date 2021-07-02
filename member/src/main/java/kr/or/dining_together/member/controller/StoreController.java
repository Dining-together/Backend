package kr.or.dining_together.member.controller;

import java.io.File;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.service.FileService;
import kr.or.dining_together.member.service.ResponseService;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: StoreController.java
 * @date : 2021/06/30 2:28 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Api(tags = {"5. Store"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class StoreController {

	private final StoreRepository storeRepository;
	private final FileService fileService;
	private final ResponseService responseService;

	@ApiOperation(value = "서류 이미지 등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/store/document")
	public CommonResult saveFile(
		@RequestBody @ApiParam(value = "서류 사진", required = true) MultipartFile file) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + "_document";
		fileService.save(file, fileName, "store/document");
		return responseService.getSuccessResult();
	}

}
