package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FilenameUtils;
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
import kr.or.dining_together.member.dto.StoreDto;
import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreImages;
import kr.or.dining_together.member.jpa.repo.StoreImagesRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.FileService;
import kr.or.dining_together.member.service.KafkaProducer;
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
	private final static String STORE_IMAGE_FOLDER_DIRECTORY = "/store/images";

	private final StoreRepository storeRepository;
	private final StoreImagesRepository storeImagesRepository;
	private final FileService fileService;
	private final ResponseService responseService;
	private final StoreService storeService;
	private final StorageService storageService;
	private final KafkaProducer kafkaProducer;

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

	@ApiOperation(value = "가게 사진 등록222")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/store/images")
	public CommonResult saveFilessss(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam("files") MultipartFile[] files) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + "_storeImages";

		AtomicInteger fileCount = new AtomicInteger(1);

		Arrays.asList(files).stream().forEach(file -> {
			String fileDirectoryName = null;
			String newFileName = fileName + fileCount.get();
			try {
				fileDirectoryName = storageService.save(file, newFileName, STORE_IMAGE_FOLDER_DIRECTORY);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileCount.getAndIncrement();

			StoreImages boardPicture = StoreImages.builder()
				.fileName(newFileName)
				.path(fileDirectoryName)
				.store(user)
				.build();

			storeImagesRepository.save(boardPicture);
			System.out.println(boardPicture);
		});

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
		user.setDocumentChecked(true);
		storeRepository.save(user);
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
		Store store = storeService.registerStore(storeRequest, email);

		StoreDto storeDto = StoreDto.builder()
			.storeType(store.getStoreType().toString())
			.addr(store.getAddr())
			.storeId(String.valueOf(store.getId()))
			.storeName(store.getName())
			.build();

		kafkaProducer.send("member-store-topic", storeDto);
		return responseService.getSingleResult(store);

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

	@ApiOperation(value = "서류 확인 (다른 서비스 호출)", notes = "업체 서류 인증 확인")
	@GetMapping(value = "/store/document")
	public boolean isDocumentChecked() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return store.getDocumentChecked();
	}

}
