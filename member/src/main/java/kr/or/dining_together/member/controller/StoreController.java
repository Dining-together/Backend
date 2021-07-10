package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.FileService;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.StorageService;
import kr.or.dining_together.member.service.StoreService;
import kr.or.dining_together.member.vo.FacilityRequest;
import kr.or.dining_together.member.vo.StoreRequest;
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
@Api(tags = {"4. Store"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class StoreController {

	private final static String STORE_DOCUMENT_FOLDER_DIRECTORY = "/store/document";
	private final static String STORE_IMAGE_FOLDER_DIRECTORY = "/store/photo";

	private final StoreRepository storeRepository;
	private final FileService fileService;
	private final ResponseService responseService;
	private final StoreService storeService;
	private final StorageService storageService;

	@ApiOperation(value = "업체 정보 조회", notes = "업체 리스트 조회")
	@GetMapping(value = "/stores")
	public ListResult<Store> getStores() throws Throwable {
		return responseService.getListResult(storeService.getStores());
	}

	@ApiOperation(value = "업체 정보 조회", notes = "업체 단건 조회")
	@GetMapping(value = "/store/{storeId}")
	public SingleResult<Store> getStore(@PathVariable long storeId) throws Throwable {
		return responseService.getSingleResult(storeService.getStore(storeId));
	}

	@ApiOperation(value = "가게 사진 등록")
	@PostMapping(value = "/store/images")
	public CommonResult saveFiles(
		@RequestParam("files") List<MultipartFile> files) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + "_storeImages";
		storageService.savefiles(files, fileName, STORE_IMAGE_FOLDER_DIRECTORY, user);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "서류 이미지 등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/store/document")
	public CommonResult saveDocument(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "서류 사진", required = true) MultipartFile file) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + "_document";
		storageService.save(file, fileName, STORE_DOCUMENT_FOLDER_DIRECTORY);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "업체 등록/수정", notes = "업체 등록한다.")
	@PostMapping("/store")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Store> registerStore(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "가게 정보", required = true) StoreRequest storeRequest
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return responseService.getSingleResult(storeService.registerStore(storeRequest, email));

	}

	@ApiOperation(value = "업체 상세 시설 등록", notes = "업체 상세 시설 등록")
	@PostMapping("/store/facility")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Facility> registerFacility(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "가게 시설", required = true) FacilityRequest facilityRequest
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return responseService.getSingleResult(storeService.registerFacility(facilityRequest, email));

	}

	@ApiOperation(value = "업체 상세 시설 수정", notes = "업체 상세 시설 수정")
	@PutMapping("/store/facility/{facilityId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Facility> modifyFacility(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@PathVariable @ApiParam(value = "시설 id", required = true) long facilityId,
		@RequestBody @ApiParam(value = "가게 시설", required = true) FacilityRequest facilityRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return responseService.getSingleResult(storeService.modifyFacility(facilityRequest, facilityId, email));
	}

}
