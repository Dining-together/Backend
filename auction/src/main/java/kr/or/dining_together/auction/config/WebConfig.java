package kr.or.dining_together.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.or.dining_together.auction.commons.interceptor.PermissionInterceptor;
import kr.or.dining_together.auction.commons.interceptor.UserMatchTokenInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private PermissionInterceptor permissionInterceptor;
	private UserMatchTokenInterceptor userMatchTokenInterceptor;

	@Autowired
	public WebConfig(@Lazy PermissionInterceptor permissionInterceptor,
		@Lazy UserMatchTokenInterceptor userMatchTokenInterceptor) {
		this.permissionInterceptor = permissionInterceptor;
		this.userMatchTokenInterceptor = userMatchTokenInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(permissionInterceptor)
			.addPathPatterns("/auction/**");

		registry.addInterceptor(userMatchTokenInterceptor)
			.addPathPatterns("/auction/**");
	}
}
