package kr.or.dining_together.auction.controller;

import java.util.ArrayList;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import kr.or.dining_together.auction.jpa.entity.SuccessBid;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.service.SuccessBidService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"3. 낙찰"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auction")
public class SuccessBidController {

	private final ResponseService responseService;
	private final SuccessBidService successBidService;
	private final UserServiceClient userServiceClient;
	private final CircuitBreakerFactory circuitBreakerFactory;

	@ApiOperation(value = "사용자별 낙찰 조회", notes = "사용자별 낙찰 불러온다.")
	@GetMapping(value = "/user/bids")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public ListResult<SuccessBid> getSuccessBidByUser(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken) {
		CircuitBreaker circuitBreaker= circuitBreakerFactory.create("circuitbreaker");
		UserIdDto userIdDto=circuitBreaker.run(()->userServiceClient.getUserId(xAuthToken),
			throwable -> null);
		if(userIdDto==null){
			return responseService.getListResult(new ArrayList<>());
		}
		return responseService.getListResult(successBidService.getSuccessbidsByUser(userIdDto));
	}

	@ApiOperation(value = "낙찰 공고 조회", notes = "낙찰된 공고 불러온다.")
	@GetMapping(value = "/bids/{successBidId}")
	public SingleResult findAuctionBySuccessBidId(
		@ApiParam(value = "낙찰 id", required = true) @PathVariable long successBidId) {
		return responseService.getSingleResult(successBidService.findAuctionBySuccessBidId(successBidId));
	}

}
