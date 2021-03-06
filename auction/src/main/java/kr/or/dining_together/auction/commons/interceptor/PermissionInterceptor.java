package kr.or.dining_together.auction.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.or.dining_together.auction.advice.exception.UnAuthorizedException;
import kr.or.dining_together.auction.advice.exception.UserNotFoundException;
import kr.or.dining_together.auction.client.UserServiceClient;
import kr.or.dining_together.auction.commons.annotation.Permission;
import kr.or.dining_together.auction.dto.UserIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PermissionInterceptor implements HandlerInterceptor {

	private final UserServiceClient userServiceClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

		HandlerMethod method = (HandlerMethod)handler;
		Permission permission = method.getMethodAnnotation(Permission.class);

		if ((!(handler instanceof HandlerMethod)) || (permission == null)) { // 호출되는 메소드가 헨들러가 아니라면 검증할 필요 없음
			return true;
		}
		String token = request.getHeader("X-AUTH-TOKEN");
		if (token == null) {
			throw new UserNotFoundException();
		}
		UserIdDto userIdDto = userServiceClient.getUserId(token);
		log.info(String.valueOf(userIdDto));
		log.info(permission.target(), userIdDto.getType());
		if (!permission.target().equals(userIdDto.getType())) {
			throw new UnAuthorizedException();
		}

		return true;
	}
}
