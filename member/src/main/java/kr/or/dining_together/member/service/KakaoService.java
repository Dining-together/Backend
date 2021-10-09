package kr.or.dining_together.member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import kr.or.dining_together.member.advice.exception.ComunicationException;
import kr.or.dining_together.member.vo.KakaoProfile;
import kr.or.dining_together.member.vo.RetKakaoAuth;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoService {

	private final RestTemplate restTemplate;
	private final Environment env;
	private final Gson gson;

	@Value("${spring.url.base}")
	private String baseUrl;

	@Value("${spring.social.kakao.client_id}")
	private String kakaoClientId;

	@Value("${spring.social.kakao.redirect}")
	private String kakaoRedirect;

	/**
	 * 카카오 플랫폼에서 사용자 정보를 요청한다.
	 **/
	public KakaoProfile getKakaoProfile(String accessToken) {
		// Set header : Content-type: application/x-www-form-urlencoded
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);

		// Set http entity
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
		try {
			// Request profile
			ResponseEntity<String> response = restTemplate.postForEntity(
				env.getProperty("spring.social.kakao.url.profile"), request, String.class);
			if (response.getStatusCode() == HttpStatus.OK)
				return gson.fromJson(response.getBody(), KakaoProfile.class);
		} catch (Exception e) {
			throw new ComunicationException();
		}
		throw new ComunicationException();
	}

	/**
	 * 카카오 플랫폼에서 사용자 토큰을 발급받는다.
	 **/
	public RetKakaoAuth getKakaoTokenInfo(String code) {
		// Set header : Content-type: application/x-www-form-urlencoded
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// Set parameter
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", "http://localhost:8000/member/social/login/kakao");
		params.add("code", code);
		System.out.print(params.values().toString());
		// Set http entity
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token",
			request, String.class);
		// return response;
		System.out.println(response.getStatusCode());
		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println(response.getBody());
			return gson.fromJson(response.getBody(), RetKakaoAuth.class);
		}
		return null;
	}

}
