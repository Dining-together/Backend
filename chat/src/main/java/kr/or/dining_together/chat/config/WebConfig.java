package kr.or.dining_together.chat.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.or.dining_together.chat.config.Filter.CORSFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public FilterRegistrationBean getFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
		registrationBean.addUrlPatterns("/**");
		return registrationBean;
	}

	// CORS 추가
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*://*")
			.allowedHeaders("*")
			.allowedMethods("*");
	}
}
