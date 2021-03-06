package kr.or.dining_together.auction.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.commons.annotation.Permission;
import kr.or.dining_together.auction.commons.annotation.UserCheck;
import kr.or.dining_together.auction.dto.AuctionDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.entity.AuctionStatus;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.AuctionKafkaProducer;
import kr.or.dining_together.auction.service.AuctionService;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.service.SuccessBidService;
import kr.or.dining_together.auction.vo.AuctionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @package : kr.or.dining_together.auction.controller
 * @name: AuctionController.java
 * @date : 2021/06/06 3:51 ??????
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Api(tags = {"1. Auction"})
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping(value = "/auction")
@Slf4j
public class AuctionController {

	private final AuctionService auctionService;
	private final SuccessBidService successBidService;
	private final ResponseService responseService;
	private final AuctionRepository auctionRepository;
	private final UserServiceClient userServiceClient;
	private final AuctionKafkaProducer auctionProducer;
	@Value(value = "${kafka.topic.auction.name}")
	private String KAFKA_AUCTION_TOPIC_NAME;

	@ApiOperation(value = "?????? ????????? ??????", notes = "?????? ????????? ????????????.")
	@GetMapping(value = "/auctions")
	public ListResult<Auction> auctions() {
		return responseService.getListResult(auctionService.getAuctions());
	}

	@ApiOperation(value = "?????? ????????? ??????", notes = "?????? ????????? ????????????.")
	@GetMapping(value = "/auctions/proceeding")
	public ListResult<Auction> auctionsProceeding() {
		return responseService.getListResult(auctionService.getAuctionsByProceeding());
	}

	@ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ????????????.")
	@GetMapping(value = "/auctions/end")
	public ListResult<Auction> auctionsEnd() {
		return responseService.getListResult(auctionService.getAuctionsByEnd());
	}

	@ApiOperation(value = "?????? ?????? ??????", notes = "?????? ?????? ????????????.")
	@GetMapping(value = "/{auctionId}")
	public SingleResult<Auction> auction(@ApiParam(value = "??????id", required = true) @PathVariable long auctionId) {

		JsonObject jsonObject = new JsonObject();
		/*
		 ** ??????????????? ??????????????? ??????.
		 */
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","info");
		jsonObject.addProperty("actionType","view");
		jsonObject.addProperty("auctionId",auctionId);
		jsonObject.addProperty("target","auction_log");

		log.info(String.valueOf(jsonObject));

		return responseService.getSingleResult(auctionService.getAuction(auctionId));
	}

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@PostMapping(value = "")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@Permission()
	public SingleResult<Auction> registerAuction(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "????????????", required = true) AuctionRequest auctionRequest) {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		log.info(String.valueOf(user));
		Auction auction = auctionService.writeAuction(user, auctionRequest);

		AuctionDto auctionDto = AuctionDto.builder()
			.auctionId(auction.getAuctionId().toString())
			.userId(String.valueOf(user.getId()))
			.title(auction.getTitle())
			.content(auction.getContent())
			.userName(user.getName())
			.path(user.getPath())
			.reservation(auction.getReservation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.userType(auction.getGroupType())
			.deadline(auction.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
			.maxPrice(auction.getMaxPrice())
			.minPrice(auction.getMinPrice())
			.storeType(auction.getStoreType())
			.build();

		auctionProducer.send(KAFKA_AUCTION_TOPIC_NAME, auctionDto);

		/*
		 ** ???????????? ????????? ???????????? ??????
		 */
		Gson gson = new Gson();
		/*
		 ** auction ????????? JsonObject ????????? ??????.
		 */
		JsonObject jsonObject = new Gson().fromJson(gson.toJson(auctionDto), JsonObject.class);
		/*
		 ** ??????????????? ??????????????? ??????.
		 */
		jsonObject.addProperty("Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		jsonObject.addProperty("UserId", user.getId());
		jsonObject.addProperty("UserName", user.getName());

		log.info("service-log :: {}", jsonObject);

		return responseService.getSingleResult(auction);
	}

	@ApiOperation(value = "???????????? ?????? ??????", notes = "???????????? ????????? ????????????.")
	@GetMapping(value = "/{userId}/auctions")
	public ListResult<Auction> getAuction(
		@PathVariable("userId") long userId) {
		return responseService.getListResult(auctionService.getAuctionsByUserId(userId));
	}

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@PutMapping(value = "/{auctionId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@UserCheck
	public SingleResult<Auction> modifyAuction(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "??????id", required = true) @PathVariable long auctionId,
		@RequestBody @ApiParam(value = "????????????", required = true) AuctionRequest auctionRequest) {
		return responseService.getSingleResult(auctionService.updateAuction(auctionId, auctionRequest));
	}

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@DeleteMapping(value = "/{auctionId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@UserCheck
	public CommonResult deleteAuction(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "??????id", required = true) @PathVariable long auctionId) {
		return responseService.getSingleResult(auctionService.deleteAuction(auctionId));
	}

	@ApiOperation(value = "?????? ??????", notes = "????????????.")
	@PostMapping(value = "/{auctionId}/success")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@UserCheck
	@Transactional
	public SingleResult successBidding(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "??????id", required = true) @PathVariable long auctionId,
		@ApiParam(value = "????????? id", required = true) @RequestParam long auctionnerId) {
		/**
		 * ????????? ?????? ?????????, ???????????? ????????? sucessbid isComplete??? 1??? ?????????.
		 */
		return responseService.getSingleResult(successBidService.writeSuccessBid(auctionId, auctionnerId));
	}

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@PostMapping(value = "/{auctionId}/end")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@UserCheck
	public CommonResult endAuction(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "??????id", required = true) @PathVariable long auctionId) {
		return responseService.getSingleResult(auctionService.endAuction(auctionId));
	}

}
