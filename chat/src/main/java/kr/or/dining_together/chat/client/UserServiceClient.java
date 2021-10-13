package kr.or.dining_together.chat.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.or.dining_together.chat.model.UserIdDto;

@FeignClient(name = "member")
public interface UserServiceClient {
	@GetMapping(value = "/member/userId")
	UserIdDto getUserId(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);
}
