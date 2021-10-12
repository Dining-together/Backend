package kr.or.dining_together.auction.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.service.FirebaseCloudMessagingService;
import kr.or.dining_together.auction.service.ResponseService;
import kr.or.dining_together.auction.vo.PushNotificationRequest;
import kr.or.dining_together.auction.vo.PushNotificationResponse;
import kr.or.dining_together.auction.vo.TokenMessageRequest;
import kr.or.dining_together.auction.vo.TopicMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"7. Alert"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class FirebaseCloudMessagingController {

	private final FirebaseCloudMessagingService firebaseCloudMessagingService;
	private final UserServiceClient userServiceClient;
	private final ResponseService responseService;

	@ApiOperation(value = "디바이스 토큰 등록", notes = "디바이스의 토큰을 DB에 저장한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/alert/register")
	public CommonResult postDeviceTokenInfo(@RequestHeader("X-AUTH-TOKEN") String xAuthToken, @RequestParam String deviceToken) throws Throwable {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		String email = user.getEmail();

		firebaseCloudMessagingService.registerDeviceToken(deviceToken, email);

		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "회원 알림 보내기", notes = "토큰에 해당하는 디바이스에 알림을 보낸다")
	@PostMapping("/notification/token")
	public ResponseEntity sendTokenNotification(@RequestBody @ApiParam(value = "메시지 정보", required = true) PushNotificationRequest request) throws
		IOException {
		firebaseCloudMessagingService.sendMessageTo(request.getToken(), request.getTitle(), request.getMessage());
		log.info(request +"has been pushed");
		return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}

	@ApiOperation(value = "회원 알림 보내기", notes = "토큰에 해당하는 디바이스에 알림을 보낸다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/alert/message")
	public CommonResult sendMessage(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "메시지 정보", required = true) TokenMessageRequest tokenMessageRequest) throws
		Throwable {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		String email = user.getEmail();

		firebaseCloudMessagingService.sendMessageByToken(email, tokenMessageRequest);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "회원 알림 구독", notes = "디바이스의 토큰을 바탕으로 알림 구독 설정을 한다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/alert/subscribe")
	public CommonResult postTopicToUser(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam @ApiParam(value = "등록하려는 토픽", required = true) String topic) throws
		Throwable {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		String email = user.getEmail();

		firebaseCloudMessagingService.subscribeTopic(email, topic);

		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "회원 알림 구독 취소", notes = "디바이스의 토큰을 바탕으로 알림 구독 취소를 한다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@PutMapping("/alert/unsubscribe")
	public CommonResult deleteTopicToUser(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam @ApiParam(value = "취소하려는 토픽", required = true) String topic) throws
		Throwable {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		String email = user.getEmail();

		firebaseCloudMessagingService.unsubscribeTopic(email, topic);

		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "구독 알림 보내기", notes = "구독에 해당하는 회원에 알림을 보낸다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/alert/message/topic")
	public CommonResult sendMessageByTopic(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "메시지 정보", required = true) TopicMessageRequest topicMessageRequest) throws
		Throwable {
		UserIdDto user = userServiceClient.getUserId(xAuthToken);
		String email = user.getEmail();

		firebaseCloudMessagingService.sendMessageByTopic(topicMessageRequest);
		return responseService.getSuccessResult();
	}

}
