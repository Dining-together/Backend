package kr.or.dining_together.auction.advice.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;
import kr.or.dining_together.auction.service.ResponseService;
import lombok.RequiredArgsConstructor;

@Component
public class FeignErrorDecoder implements ErrorDecoder {


	@Override
	public Exception decode(String methodKey, Response response) {
		switch(response.status()) {
			case 400:
				break;
			case 404:
				if (methodKey.contains("getUserId")) {
					return new UserNotFoundException();
				}
				break;
			default:
				return new Exception(response.reason());
		}

		return null;
	}
}