package kr.or.dining_together.auction.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auctioneer;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.AuctioneerService;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.vo.AuctioneerRequest;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.auction.controller
 * @name: AuctioneerController.java
 * @date : 2021/06/16 2:12 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 공고 참여 업체 컨트롤러
 * @modified :
 **/
@Api(tags = {"2. Auctioneer"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auction")
public class AuctioneerController {
	private final AuctioneerService auctioneerService;
	private final ResponseService responseService;
	private final UserServiceClient userServiceClient;

	@ApiOperation(value = "참여 업체 리스트 조회", notes = "공고에 참여힌 업체 리스트 조회한다.")
	@GetMapping(value = "/{auctionId}/store")
	public ListResult<Auctioneer> auctioneers(
		@ApiParam(value = "공고id", required = true) @PathVariable long auctionId
	) {
		return responseService.getListResult(auctioneerService.getAuctioneer(auctionId));
	}

	@ApiOperation(value = "경매에 업체 참여", notes = "경매에 업체가 참여한다.")
	@PostMapping("/{auctionId}/store")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Auctioneer> registerAuctioneer(
		@ApiParam(value = "공고id", required = true) @PathVariable long auctionId,
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "경매 참여 등록 정보", required = true) AuctioneerRequest auctioneerRequest) {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		return responseService.getSingleResult(
			auctioneerService.registerAuctioneer(auctioneerRequest, user, auctionId));

	}

	@ApiOperation(value = "참여 업체 수정", notes = "공고에 참여한 업체를 수정 한다.")
	@PutMapping(value = "/{auctioneerId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Auctioneer> modifyAuction(
		@ApiParam(value = "공고id", required = true) @PathVariable long auctioneerId,
		@RequestBody @ApiParam(value = "경매 참여 등록 정보", required = true) AuctioneerRequest auctioneerRequest) {
		return responseService.getSingleResult(auctioneerService.modifyAuctioneer(auctioneerRequest, auctioneerId));
	}

	@ApiOperation(value = "참여 업체 삭제", notes = "공고에 참여한 업체를 삭제 한다.")
	@DeleteMapping(value = "/{auctioneerId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public CommonResult deleteAuction(
		@ApiParam(value = "공고 참여 업체 id", required = true) @PathVariable long auctioneerId) {
		return responseService.getSingleResult(auctioneerService.deleteAuctioneer(auctioneerId));
	}

}
