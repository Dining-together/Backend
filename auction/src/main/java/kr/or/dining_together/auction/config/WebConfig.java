package kr.or.dining_together.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.or.dining_together.auction.commons.interceptor.PermissionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private PermissionInterceptor permissionInterceptor;

	@Autowired
	public WebConfig(@Lazy PermissionInterceptor permissionInterceptor) {
		this.permissionInterceptor = permissionInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(permissionInterceptor)
			.addPathPatterns("/auction/**");
	}
}
