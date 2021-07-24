package kr.or.dining_together.auction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.or.dining_together.auction.jpa.entity.SuccessBid;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.service.SuccessBidService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"3. 낙찰"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bid")
public class SuccessBidController {

	private final ResponseService responseService;
	private final SuccessBidService successBidService;

	@ApiOperation(value = "사용자별 낙찰 조회", notes = "사용자별 낙찰 불러온다.")
	@GetMapping(value = "/{userId}")
	public ListResult<SuccessBid> getAuction(
		@PathVariable("userId") long userId) {
		return responseService.getListResult(successBidService.getSuccessbidsByUser(userId));
	}

}
