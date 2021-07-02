package kr.or.dining_together.auction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.or.dining_together.auction.dto.UserIdDto;

@FeignClient(name = "member")
public interface UserServiceClient {
	@GetMapping(value = "/member/userId")
	public UserIdDto getUserId(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);
}
