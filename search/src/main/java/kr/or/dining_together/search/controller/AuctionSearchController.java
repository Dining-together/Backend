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
import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.dto.AuctionDto;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.AuctionService;
import kr.or.dining_together.search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"1. AuctionSearch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
@Slf4j
public class AuctionSearchController {

	private final AuctionService auctionService;
	private final ResponseService responseService;

	@ApiOperation(value = "단일 등록", notes = "단일 경매 공고를 등록한다")
	@PostMapping(value = "/auction")
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "경매공고 정보", required = true) AuctionDto auctionDto) {
		auctionService.createAuctionIndex(auctionDto);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@GetMapping(value = "/auction")
	public ListResult<Auction> gettingSearchResults(
		@RequestParam @ApiParam(value = "검색 키워드", required = true) String keyword) {
		/**
		 * json 객체를 만들어 로깅, 검색 결과가 없을수도 있어서 먼저로깅.
		 */
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","info");
		jsonObject.addProperty("category","auction");
		jsonObject.addProperty("target","search_log");
		jsonObject.addProperty("keyword", keyword);

		log.info(String.valueOf(jsonObject));
		List<Auction> auctions = auctionService.findByTitleMatchingNames(keyword);


		return responseService.getListResult(auctions);
	}
}
