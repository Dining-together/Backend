package kr.or.dining_together.search.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.ResponseService;
import kr.or.dining_together.search.service.StoreService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"2. StoreSearch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
public class StoreSearchController {

	private final StoreService storeService;
	private final ResponseService responseService;

	@ApiOperation(value = "단일 등록", notes = "단일 가게를 등록한다")
	@PostMapping(value = "/store")
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "가게 정보", required = true) Store store) {
		storeService.createStoreIndex(store);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "단일 삭제", notes = "단일 경매 공고를 삭제한다")
	@DeleteMapping(value = "/store")
	public CommonResult deletingAuction(@ApiParam(value = "가게 id", required = true) String id) throws IOException {
		storeService.deleteStoreDocument(id);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@GetMapping(value = "/store")
	public ListResult<Store> gettingSearchResults(@ApiParam(value = "검색 키워드", required = true) String keyword) {
		List<Store> stores = storeService.findByTitle(keyword);
		return responseService.getListResult(stores);
	}
}
