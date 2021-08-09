package kr.or.dining_together.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.or.dining_together.search.dto.UserIdDto;

@FeignClient(name = "member")
public interface UserServiceClient {
	@GetMapping(value = "/member/userId")
	UserIdDto getUserId(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);

	@GetMapping(value = "/member/store/document")
	boolean isDocumentChecked(@RequestHeader("X-AUTH-TOKEN") String xAuthToken);
}
