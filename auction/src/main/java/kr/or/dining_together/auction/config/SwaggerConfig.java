package kr.or.dining_together.auction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @package : kr.or.dining_together.auction.advice
 * @name: ExceptionAdvice.java
 * @date : 2021/06/05 10:00 오후
 * @author : jifrozen
 * @version : 1.0.0
 * @description : swagger 설정 파일
 * @modified :
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
			.apis(RequestHandlerSelectors.basePackage("kr.or.dining_together.auction.controller"))
			.paths(PathSelectors.ant("/auction/**"))
			.build()
			.useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
	}

	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder().title("Auction API Documentation")
			.description("공고 API 문서")
			.version("1").build();
	}
}
