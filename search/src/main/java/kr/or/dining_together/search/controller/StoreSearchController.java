package kr.or.dining_together.search.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.client.UserServiceClient;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.dto.UserIdDto;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.ResponseService;
import kr.or.dining_together.search.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"2. StoreSearch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
@Slf4j
public class StoreSearchController {

	private final StoreService storeService;
	private final ResponseService responseService;
	private final UserServiceClient userServiceClient;

	@ApiOperation(value = "단일 등록", notes = "단일 가게를 등록한다")
	@PostMapping(value = "/store")
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "가게 정보", required = true) Store store) {
		storeService.createStoreIndex(store);
		return responseService.getSuccessResult();
	}

	// @ApiOperation(value = "단일 삭제", notes = "단일 경매 공고를 삭제한다")
	// @DeleteMapping(value = "/store")
	// public CommonResult deletingAuction(@RequestParam @ApiParam(value = "가게 id", required = true) String id) throws IOException {
	// 	storeService.deleteStoreDocument(id);
	// 	return responseService.getSuccessResult();
	// }

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping(value = "/store")
	public ListResult<Store> gettingSearchResults(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,@RequestParam @ApiParam(value = "검색 키워드", required = true) String keyword) {
		List<Store> stores = storeService.findByTitleMatchingNames(keyword);
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		/*
		 ** 사용자가 클릭한 가게정보 로깅
		 */
		Gson gson=new Gson();
		/*
		 ** store 객체를 JsonObject 객체로 변경.
		 */
		JsonObject jsonObject=new Gson().fromJson(gson.toJson(stores.get(0)),JsonObject.class);
		/*
		 ** 현재시간을 포맷팅하여 추가.
		 */
		jsonObject.addProperty("Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		jsonObject.addProperty("UserId",user.getId());
		jsonObject.addProperty("UserName",user.getName());
		log.info("service-log :: {}",jsonObject);

		return responseService.getListResult(stores);
	}
}
