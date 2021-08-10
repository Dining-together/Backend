package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.StorageService;
import kr.or.dining_together.member.service.KafkaProducer;
import kr.or.dining_together.member.service.StoreService;
import kr.or.dining_together.member.vo.FacilityRequest;
import kr.or.dining_together.member.vo.StoreListResponse;
import kr.or.dining_together.member.vo.StoreRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class StoreController {

	@Value(value = "${kafka.topic.store.name}")
	private String KAFKA_STORE_TOPIC_NAME;

	private final static String STORE_DOCUMENT_FOLDER_DIRECTORY = "/store/document";
	private final static String STORE_IMAGE_FOLDER_DIRECTORY = "/store/images";
	private final static String STORE_DOCUMENT_FILES_POSTFIX = "_document";
	private final static String STORE_IMAGE_FILES_POSTFIX = "_storeImages";

	private final StoreRepository storeRepository;
	private final StoreImagesRepository storeImagesRepository;
	private final ResponseService responseService;
	private final StoreService storeService;
	private final StorageService storageService;
	private final KafkaProducer storeProducer;

	@ApiOperation(value = "업체 정보 조회", notes = "업체 리스트 조회")
	@GetMapping(value = "/stores")
	public ListResult<StoreListResponse> getStores() throws Throwable {
		return responseService.getListResult(storeService.getStores());
	}

	@ApiOperation(value = "업체 정보 조회", notes = "업체 단건 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping(value = "/store/{storeId}")
	public SingleResult<Store> getStore(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@PathVariable long storeId) throws Throwable {
		//업체 정보 조회시 이미지 url도 같이 넘겨주기.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		/*
		 ** 사용자가 클릭한 가게정보 로깅
		 */
		Store store = storeService.getStore(storeId);
		Gson gson = new Gson();
		/*
		 ** store 객체를 JsonObject 객체로 변경.
		 */
		JsonObject jsonObject = new Gson().fromJson(gson.toJson(store), JsonObject.class);
		/*
		 ** 현재시간을 포맷팅하여 추가.
		 */
		jsonObject.addProperty("Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		jsonObject.addProperty("Email", email);
		log.info("service-log :: {}", jsonObject);

		return responseService.getSingleResult(storeService.getStore(storeId));
	}

	@ApiOperation(value = "가게 사진 등록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/store/images")
	public CommonResult saveFiles(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam("files") MultipartFile[] files) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store user = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

		if (user.getPath() != null) {
			new File(user.getPath()).delete();
		}
		String fileName = user.getId() + STORE_IMAGE_FILES_POSTFIX;

		AtomicInteger fileCount = new AtomicInteger(1);

		Arrays.asList(files).stream().forEach(file -> {
			String fullFilePath = null;
			String newFileName = fileName + fileCount.get();
			try {
				fullFilePath = storageService.save(file, newFileName, STORE_IMAGE_FOLDER_DIRECTORY);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileCount.getAndIncrement();

			StoreImages boardPicture = StoreImages.builder()
				.fileName(newFileName)
				.path(fullFilePath)
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
		String fileName = user.getId() + STORE_DOCUMENT_FILES_POSTFIX;
		String fullFilePath = storageService.save(file, fileName, STORE_DOCUMENT_FOLDER_DIRECTORY);

		user.setDocumentChecked(true);
		user.setDocumentFilePath(fullFilePath);

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
			.storeName(store.getStoreName())
			.comment(store.getComment())
			.storeId(String.valueOf(store.getId()))
			.addr(store.getAddr())
			.storeType(store.getStoreType().toString())
			.openTime(store.getOpenTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.phoneNum(store.getPhoneNum())
			.closedTime(store.getClosedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.longitude(store.getLongitude())
			.latitude(store.getLatitude())
			.storeImagePath(store.getPath())
			.build();

		storeProducer.send(KAFKA_STORE_TOPIC_NAME, storeDto);
		return responseService.getSingleResult(store);

	}

	@ApiOperation(value = "업체 상세 시설 등록/수정", notes = "업체 상세 시설 등록")
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


	@ApiOperation(value = "서류 확인 (다른 서비스 호출)", notes = "업체 서류 인증 확인")
	@GetMapping(value = "/store/document")
	public boolean isDocumentChecked() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return store.getDocumentChecked();
	}

}
