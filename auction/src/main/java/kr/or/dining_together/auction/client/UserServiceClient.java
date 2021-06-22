package kr.or.dining_together.auction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.model.CommonResult;
import kr.or.dining_together.auction.vo.LoginRequest;
import kr.or.dining_together.auction.vo.SignUpRequest;

@FeignClient(name = "member")
public interface UserServiceClient {
	@GetMapping(value = "/member/userId")
	UserIdDto getUserId(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);

	// @PostMapping(path = "/member/auth/signin")
	// public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest);
	//
	// @PostMapping(path = "/member/auth/signup")
	// public void userSignUp(@RequestBody SignUpRequest sign);

}
