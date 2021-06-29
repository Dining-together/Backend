package kr.or.dining_together.member.controller;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import io.swagger.annotations.Api;
import kr.or.dining_together.member.service.KakaoService;
import kr.or.dining_together.member.service.NaverService;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: SocialController.java
 * @date : 2021/06/03 1:24 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 소셜 로그인을 위해 요청
 * @modified :
 **/
@Api(tags = {"2. social login"})
@Controller
@RequiredArgsConstructor
@RequestMapping("/member/social/login")
public class SocialController {

	private final Environment env;
	private final RestTemplate restTemplate;
	private final Gson gson;
	private final KakaoService kakaoService;
	private final NaverService naverService;

	@Value("${spring.url.base}")
	private String baseUrl;
	@Value("${spring.social.kakao.client_id}")
	private String kakaoClientId;
	@Value("${spring.social.kakao.redirect}")
	private String kakaoRedirect;
	@Value("${spring.social.naver.client_id}")
	private String naverClientId;

	@Value("${spring.social.naver.client_secret}")
	private String naverClientSecret;

	@Value("${spring.social.naver.redirect}")
	private String naverRedirect;

	@GetMapping
	public ModelAndView socialLogin(ModelAndView mav) {
		StringBuilder kakaoLoginUrl = new StringBuilder()
			.append(env.getProperty("spring.social.kakao.url.login"))
			.append("?client_id=").append(kakaoClientId)
			.append("&response_type=code")
			.append("&redirect_uri=").append(baseUrl).append(kakaoRedirect);

		mav.addObject("kakaoLoginUrl", kakaoLoginUrl);

		SecureRandom random = new SecureRandom();
		String state = new BigInteger(130, random).toString();

		StringBuilder naverLoginUrl = new StringBuilder()
			.append(env.getProperty("spring.social.naver.url.login"))
			.append("?response_type=code")
			.append("&client_id=").append(naverClientId)
			.append("&redirect_uri=").append(baseUrl).append(naverRedirect)
			.append("&state=").append(state);

		mav.addObject("naverLoginUrl", naverLoginUrl);

		mav.setViewName("member/social/login");

		return mav;
	}

	@GetMapping(value = "/kakao")
	public ModelAndView redirectKakao(ModelAndView mav, @RequestParam String code) {
		mav.addObject("authInfo", kakaoService.getKakaoTokenInfo(code));
		mav.setViewName("member/social/redirectKakao");
		return mav;
	}

	@GetMapping(value = "/naver")
	public ModelAndView redirectNaver(ModelAndView mav, @RequestParam String code, @RequestParam String state) {
		mav.addObject("authInfo", naverService.getNaverTokenInfo(code, state));
		mav.setViewName("member/social/redirectNaver");
		return mav;
	}

}
