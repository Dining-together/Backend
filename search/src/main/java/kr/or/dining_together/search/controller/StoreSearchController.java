package kr.or.dining_together.search.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import kr.or.dining_together.search.advice.exception.ResourceNotExistException;
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

	@ApiOperation(value = "단일 등록", notes = "단일 가게를 등록한다")
	@PostMapping(value = "/store")
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "가게 정보", required = true) Store store) {
		storeService.createStoreIndex(store);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@GetMapping(value = "/store")
	public ListResult<Store> gettingSearchResults(
		@RequestParam @ApiParam(value = "검색 키워드", required = true) String keyword) {
		List<Store> stores = storeService.findByTitleMatchingNames(keyword);

		return responseService.getListResult(stores);
	}
}
