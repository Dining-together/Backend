package kr.or.dining_together.member.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @package : kr.or.dining_together.member.vo
 * @name: RetKakaoAuth.java
 * @date : 2021/06/03 1:30 오전
 * @author : jifrozen
 * @version : 1.0.0
 * @description : 카카오 token api 연동시 맵핑을 위한 모델 생성
 * @modified :
 **/

@Getter
@Setter
public class RetKakaoAuth {
	private String access_token;
	private String token_type;
	private String refresh_token;
	private long expires_in;
	private String scope;
}
