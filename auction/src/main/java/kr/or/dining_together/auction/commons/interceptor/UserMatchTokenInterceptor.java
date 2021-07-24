package kr.or.dining_together.auction.commons.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import kr.or.dining_together.auction.advice.exception.ResourceNotExistException;
import kr.or.dining_together.auction.advice.exception.UserNotFoundException;
import kr.or.dining_together.auction.advice.exception.UserNotMatchedException;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.dto.UserIdDto;
import kr.or.dining_together.auction.jpa.entity.Auction;
import kr.or.dining_together.auction.jpa.repo.AuctionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserMatchTokenInterceptor implements HandlerInterceptor {

	private final UserServiceClient userServiceClient;
	private final AuctionRepository auctionRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String httpMethod = request.getMethod();

		if (httpMethod.equals("DELETE") || httpMethod.equals("PUT")) {
			Map<?, ?> pathVariables = (Map<?, ?>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			Long id = Long.parseLong((String)pathVariables.get("auctionId"));
			Auction auction = auctionRepository.findById(id).orElseThrow(ResourceNotExistException::new);
			String token = request.getHeader("X-AUTH-TOKEN");
			if (token == null) {
				throw new UserNotFoundException();
			}
			UserIdDto userIdDto = userServiceClient.getUserId(token);
			if (auction.getUserId() != userIdDto.getId()) {
				throw new UserNotMatchedException();
			}
		}

		return true;
	}
}
