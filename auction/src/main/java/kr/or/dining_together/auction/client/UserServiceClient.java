package kr.or.dining_together.auction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.or.dining_together.auction.dto.UserIdDto;

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
