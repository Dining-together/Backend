package kr.or.dining_together.search.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.AuctionService;
import kr.or.dining_together.search.service.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. AuctionSearch"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
public class AuctionSearchController {

	private final AuctionService auctionService;
	private final ResponseService responseService;

	@ApiOperation(value = "단일 등록", notes = "단일 경매 공고를 등록한다")
	@PostMapping(value = "/auction")
	public CommonResult indexingAuction(@RequestBody @ApiParam(value = "경매공고 정보", required = true) Auction auction) {
		auctionService.createAuctionIndex(auction);
		return responseService.getSuccessResult();
	}

	// @ApiOperation(value = "단일 삭제", notes = "단일 경매 공고를 삭제한다")
	// @DeleteMapping(value = "/auction")
	// public CommonResult deletingAuction(@RequestParam @ApiParam(value = "경매공고 id", required = true) String id) throws IOException {
	// 	auctionService.deleteAuctionDocument(id);
	// 	return responseService.getSuccessResult();
	// }

	@ApiOperation(value = "제목 검색", notes = "경매 공고를 키워드로 검색한다.")
	@GetMapping(value = "/auction")
	public ListResult<Auction> gettingSearchResults(@RequestParam @ApiParam(value = "검색 키워드", required = true) String keyword) {
		List<Auction> auctions = auctionService.findByTitleMatchingNames(keyword);
		return responseService.getListResult(auctions);
	}
}
