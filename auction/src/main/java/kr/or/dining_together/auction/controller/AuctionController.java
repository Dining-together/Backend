package kr.or.dining_together.auction.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.AuctionService;
import kr.or.dining_together.auction.service.ResponseService;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.auction.controller
 * @name: AuctionController.java
 * @date : 2021/06/06 3:51 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Api(tags = {"1. Auction"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auction")
public class AuctionController {

	private final AuctionService auctionService;
	private final ResponseService responseService;

	@ApiOperation(value = "공고 리스트 조회", notes = "공고 리스트 조회한다.")
	@GetMapping(value = "/auctions")
	public ListResult<Auction> auctions() {
		return responseService.getListResult(auctionService.getAuctions());
	}

	@ApiOperation(value = "공고 단건 조회", notes = "공고 단건 조회한다.")
	@GetMapping(value = "/{auctionId}")
	public SingleResult<Auction> auction(@ApiParam(value = "공고id", required = true) @PathVariable long auctionId) {
		return responseService.getSingleResult(auctionService.getAuction(auctionId));
	}

	@ApiOperation(value = "공고 작성", notes = "공고 작성 한다.")
	@PostMapping(value = "")
	public SingleResult<Auction> registerAuction(
		@RequestBody @ApiParam(value = "공고정보", required = true) AuctionDto auctionDto) {
		return responseService.getSingleResult(auctionService.writeAuction(auctionDto));
	}

	@ApiOperation(value = "공고 수정", notes = "공고 수정 한다.")
	@PutMapping(value = "/{auctionId}")
	public SingleResult<Auction> modifyAuction(@ApiParam(value = "공고id", required = true) @PathVariable long auctionId,
		@RequestBody @ApiParam(value = "공고정보", required = true) AuctionDto auctionDto) {
		return responseService.getSingleResult(auctionService.updateAuction(auctionId, auctionDto));
	}

	@ApiOperation(value = "공고 삭제", notes = "공고 삭제 한다.")
	@DeleteMapping(value = "/{auctionId}")
	public CommonResult deleteAuction(@ApiParam(value = "공고id", required = true) @PathVariable long auctionId) {
		return responseService.getSingleResult(auctionService.deleteAuction(auctionId));
	}

}
