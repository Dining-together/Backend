package kr.or.dining_together.auction.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import kr.or.dining_together.auction.commons.annotation.Permission;
import kr.or.dining_together.auction.dto.ReviewDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Review;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.service.ReviewService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"4. Review"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auction")
public class ReviewController {

	private final ResponseService responseService;
	private final ReviewService reviewService;
	private final UserServiceClient userServiceClient;

	@ApiOperation(value = "사용자별 리뷰 조회", notes = "사용자별 리뷰를 조회한다.")
	@GetMapping("/reviews/user/{userId}")
	public ListResult<Review> getReviewsByUser(@ApiParam(value = "사용자id", required = true) @PathVariable long userId) {
		return responseService.getListResult(reviewService.getReviewsByUser(userId));
	}

	@ApiOperation(value = "업체별 리뷰 조회", notes = "업체별 리뷰를 조회한다.")
	@GetMapping("/reviews/store/{storeId}")
	@Permission(target = "STORE")
	public ListResult<Review> getReviewsByStore(@ApiParam(value = "업체id", required = true) @PathVariable long storeId) {
		return responseService.getListResult(reviewService.getReviewsByStore(storeId));
	}

	@ApiOperation(value = "리뷰 작성", notes = "리뷰 작성 한다.")
	@PostMapping(value = "/{successBidId}/review")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@Permission(target = "CUSTOMER")
	public SingleResult<Review> registerReview(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "낙찰 id", required = true) @PathVariable long successBidId,
		@RequestBody @ApiParam(value = "리뷰", required = true) ReviewDto reviewDto

	) {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		return responseService.getSingleResult(reviewService.writeReview(reviewDto, successBidId, user));
	}

	@ApiOperation(value = "리뷰 삭제", notes = "리뷰 삭제 한다.")
	@DeleteMapping(value = "/{reviewId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public CommonResult deleteReview(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "리뷰 Id", required = true) @PathVariable long reviewId) {
		return responseService.getSingleResult(reviewService.deleteReview(reviewId));
	}

}
