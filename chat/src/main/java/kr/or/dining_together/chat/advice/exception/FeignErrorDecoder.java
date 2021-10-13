package kr.or.dining_together.chat.advice.exception;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		switch (response.status()) {
			case 400:
				return new BadRequestException();
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