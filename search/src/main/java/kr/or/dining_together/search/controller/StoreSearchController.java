package kr.or.dining_together.search.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.dto.StoreDto;
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
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "가게 정보", required = true) StoreDto storeDto) {
		storeService.createStoreIndex(storeDto);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@GetMapping(value = "/store")
	public ListResult<Store> gettingSearchResults(
		@RequestParam @ApiParam(value = "검색 키워드", required = true) String keyword) {
		/**
		 * json 객체를 만들어 로깅, 검색 결과가 없을수도 있어서 먼저로깅.
		 */
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","info");
		jsonObject.addProperty("category","store");
		jsonObject.addProperty("target","search_log");
		jsonObject.addProperty("keyword", keyword);

		log.info(String.valueOf(jsonObject));

		List<Store> stores = storeService.findByTitleMatchingNames(keyword);


		return responseService.getListResult(stores);
	}
}
