package kr.or.dining_together.member.config;

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
 * @package : kr.or.dining_together.member.config
 * @name: SwaggerConfig.java
 * @date : 2021/05/26 12:44 오전
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
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build();
		// return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
		// 	.apis(RequestHandlerSelectors.basePackage("kr.or.dining_together.member.controller"))
		// 	.paths(PathSelectors.ant("/member/**"))
		// 	.build()
		// 	.useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
	}

	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder().title("Member API Documentation")
			.description("회원 API 문서")
			.version("1").build();
	}
}
