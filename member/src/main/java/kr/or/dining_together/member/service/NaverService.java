package kr.or.dining_together.member.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

import kr.or.dining_together.member.vo.NaverProfile;
import kr.or.dining_together.member.vo.RetNaverAuth;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NaverService {

	private final RestTemplate restTemplate;
	private final Environment env;
	private final Gson gson;

	@Value("${spring.url.base}")
	private String baseUrl;

	@Value("${spring.social.naver.client_id}")
	private String naverClientId;

	@Value("${spring.social.naver.client_secret}")
	private String naverClientSecret;

	@Value("${spring.social.naver.redirect}")
	private String naverRedirect;

	@Value("${spring.social.naver.url.profile}")
	private String naverProfile;

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

	public NaverProfile getNaverProfile(String accessToken) {
		String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가
		System.out.println(header);
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);
		String responseBody = get(naverProfile, requestHeaders);
		System.out.println(responseBody);
		return gson.fromJson(responseBody, NaverProfile.class);
	}

	public RetNaverAuth getNaverTokenInfo(String code) {
		// Set header : Content-type: application/x-www-form-urlencoded
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// Set parameter
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", naverClientId);
		params.add("client_secret", naverClientSecret);
		params.add("redirect_uri", "http://192.168.219.109:8000/member/social/login/naver");
		params.add("code", code);
		params.add("state", "dining");
		// Set http entity
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.naver.url.token"),
			request, String.class);
		System.out.println(response.getStatusCode());
		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println(response.getBody());
			return gson.fromJson(response.getBody(), RetNaverAuth.class);
		}
		return null;
	}
}

