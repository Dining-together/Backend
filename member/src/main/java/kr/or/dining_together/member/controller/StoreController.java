package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Facility;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreImages;
import kr.or.dining_together.member.jpa.entity.User;
import kr.or.dining_together.member.jpa.repo.StoreImagesRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.jpa.repo.UserDeviceTokenRepository;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.FirebaseCloudMessagingService;
import kr.or.dining_together.member.service.KafkaProducer;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.StorageService;
import kr.or.dining_together.member.service.StoreService;
import kr.or.dining_together.member.vo.FacilityRequest;
import kr.or.dining_together.member.vo.StoreListResponse;
import kr.or.dining_together.member.vo.StoreRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: StoreController.java
 * @date : 2021/06/30 2:28 ??????
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

	private final static String STORE_DOCUMENT_FOLDER_DIRECTORY = "/store/document";
	private final static String STORE_IMAGE_FOLDER_DIRECTORY = "/store/images";
	private final static String STORE_DOCUMENT_FILES_POSTFIX = "_document";
	private final static String STORE_IMAGE_FILES_POSTFIX = "_storeImages";
	private final static String STORE_PUSH_TOPIC_NAME="store";
	private final StoreRepository storeRepository;
	private final StoreImagesRepository storeImagesRepository;
	private final UserRepository userRepository;
	private final ResponseService responseService;
	private final StoreService storeService;
	private final StorageService storageService;
	private final KafkaProducer storeProducer;
	private final UserDeviceTokenRepository userDeviceTokenRepository;
	private final FirebaseCloudMessagingService firebaseCloudMessagingService;



	@Value(value = "${kafka.topic.store.name}")
	private String KAFKA_STORE_TOPIC_NAME;

	@ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ??????")
	@GetMapping(value = "/stores")
	public ListResult<StoreListResponse> getStores() throws Throwable {
		return responseService.getListResult(storeService.getStores());
	}

	@ApiOperation(value = "?????? ?????? ??????", notes = "?????? ?????? ??????")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? access_token", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping(value = "/store/{storeId}")
	public SingleResult<Store> getStore(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@PathVariable long storeId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		Optional<User> user = userRepository.findByEmail(email);

		/**
		 * Make Tracking log when customer request endpoint.
		 */
		if (user.get().getType().equals("CUSTOMER")) {
			Store store = storeService.getStore(storeId);
			Gson gson = new Gson();
			Optional<Customer> customer = userRepository.findByEmail(email);
			/*
			 ** store ????????? JsonObject ????????? ??????.
			 */
			JsonObject jsonObject = new JsonObject();
			/*
			 ** ??????????????? ??????????????? ??????.
			 */
			jsonObject.addProperty("msgType", "tracking");
			jsonObject.addProperty("logType", "info");
			jsonObject.addProperty("actionType", "view");
			jsonObject.addProperty("target", "store_log");
			jsonObject.addProperty("storeId", storeId);
			jsonObject.addProperty("email", email);
			jsonObject.addProperty("gender", customer.get().getGender());

			log.info(String.valueOf(jsonObject));
		}

		return responseService.getSingleResult(storeService.getStore(storeId));
	}

	@ApiOperation(value = "?????? ?????? ??????")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? access_token", required = true, dataType = "String", paramType = "header")
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

	@ApiOperation(value = "?????? ????????? ??????")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? access_token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/store/document")
	public CommonResult saveDocument(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "?????? ??????", required = true) MultipartFile file) throws IOException {
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

	@ApiOperation(value = "?????? ??????/??????", notes = "?????? ????????????.")
	@PostMapping("/store")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Store> registerStore(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "?????? ??????", required = true) StoreRequest storeRequest
	) throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store store = storeService.registerStore(storeRequest, email);
		Optional<User> user = userRepository.findByEmail(email);
		StoreDto storeDto = StoreDto.builder()
			.storeName(user.get().getName())
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
		firebaseCloudMessagingService.subscribeTopic(email,STORE_PUSH_TOPIC_NAME);
		return responseService.getSingleResult(store);

	}

	@ApiOperation(value = "?????? ?????? ?????? ??????/??????", notes = "?????? ?????? ?????? ??????")
	@PostMapping("/store/facility")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Facility> registerFacility(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "?????? ??????", required = true) FacilityRequest facilityRequest
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return responseService.getSingleResult(storeService.registerFacility(facilityRequest, email));

	}

	@ApiOperation(value = "?????? ?????? (?????? ????????? ??????)", notes = "?????? ?????? ?????? ??????")
	@GetMapping(value = "/store/document")
	public boolean isDocumentChecked() throws Throwable {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return store.getDocumentChecked();
	}

}
