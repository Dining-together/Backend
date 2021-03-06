package kr.or.dining_together.auction.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.ReviewDto;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Review;
import kr.or.dining_together.auction.jpa.entity.ReviewImages;
import kr.or.dining_together.auction.jpa.repo.ReviewImagesRepository;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.model.ListResult;
import kr.or.dining_together.auction.model.SingleResult;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.service.ReviewService;
import kr.or.dining_together.auction.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.json.Json;

@Api(tags = {"4. Review"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auction")
@Slf4j
public class ReviewController {

	private static String REVIEW_FOLDER_DIRECTORY = "/review/images";

	private final ResponseService responseService;
	private final ReviewService reviewService;
	private final UserServiceClient userServiceClient;
	private final StorageService storageService;
	private final ReviewImagesRepository reviewImagesRepository;

	@ApiOperation(value = "???????????? ?????? ??????", notes = "???????????? ????????? ????????????.")
	@GetMapping("/reviews/user/{userId}")
	public ListResult<Review> getReviewsByUser(@ApiParam(value = "?????????id", required = true) @PathVariable long userId) {
		return responseService.getListResult(reviewService.getReviewsByUser(userId));
	}

	@ApiOperation(value = "????????? ?????? ??????", notes = "????????? ????????? ????????????.")
	@GetMapping("/reviews/store/{storeId}")
	public ListResult<Review> getReviewsByStore(@ApiParam(value = "??????id", required = true) @PathVariable long storeId) {
		return responseService.getListResult(reviewService.getReviewsByStore(storeId));
	}

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@DeleteMapping(value = "/review/{reviewId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	public CommonResult deleteReview(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@ApiParam(value = "?????? Id", required = true) @PathVariable long reviewId) {
		return responseService.getSingleResult(reviewService.deleteReview(reviewId));
	}

	// @ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	// @PutMapping(value = "/review/{reviewId}")
	// @ApiImplicitParams({
	// 	@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	// })
	// @UserCheck
	// public SingleResult<Review> modifyReview(
	// 	@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
	// 	@RequestParam("reviewId") @PathVariable("reviewId") long reviewId,
	// 	@RequestParam("content") String content,
	// 	@RequestParam("score") int score,
	// 	@RequestParam("files") List<MultipartFile> files) throws IOException {
	// 	UserIdDto user = userServiceClient.getUserId(xAuthToken);
	// 	ReviewDto reviewDto = ReviewDto.builder()
	// 		.content(content)
	// 		.score(score)
	// 		.build();
	// 	Review review = reviewService.modifyReview(reviewDto, reviewId);
	// 	if (!review.getReviewImages().isEmpty()){
	// 		reviewImagesRepository.deleteAllByReview(review);
	// 	}
	// 	String fileName = review.getReviewId() + "_" + user.getName();
	// 	fileService.savefiles(files, fileName, "review/images", review);
	// 	return responseService.getSingleResult(review);
	// }

	@ApiOperation(value = "?????? ??????", notes = "?????? ?????? ??????.")
	@PostMapping(value = "/{successBidId}/review")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "????????? ?????? ??? jwt token", required = true, dataType = "String", paramType = "header")
	})
	@Transactional
	public SingleResult<Review> registerReview(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam("successBidId") @PathVariable("successBidId") long successBidId,
		@RequestParam("content") String content,
		@RequestParam("score") int score,
		@RequestParam("files") MultipartFile[] files

	) {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		ReviewDto reviewDto = ReviewDto.builder()
			.content(content)
			.score(score)
			.build();
		Review review = reviewService.writeReview(reviewDto, successBidId, user);

		String fileName = review.getReviewId() + "_" + user.getName();

		AtomicInteger fileCount = new AtomicInteger(1);

		Arrays.asList(files).stream().forEach(file -> {
			String fileDirectoryName = null;
			String newFileName = fileName + fileCount.get();

			try {
				fileDirectoryName = storageService.save(file, newFileName, REVIEW_FOLDER_DIRECTORY);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileCount.getAndIncrement();

			ReviewImages boardPicture = ReviewImages.builder()
				.fileName(newFileName)
				.path(fileDirectoryName)
				.review(review)
				.build();
			reviewImagesRepository.save(boardPicture);
		});

		JsonObject jsonObject = new JsonObject();
		/*
		 ** ??????????????? ??????????????? ??????.
		 */
		jsonObject.addProperty("msgType","tracking");
		jsonObject.addProperty("logType","info");
		jsonObject.addProperty("actionType","review");
		jsonObject.addProperty("target","review_log");
		jsonObject.addProperty("storeId",review.getStoreId());
		jsonObject.addProperty("email", user.getName());
		jsonObject.addProperty("score",review.getScore());

		log.info(String.valueOf(jsonObject));

		return responseService.getSingleResult(review);
	}

}
