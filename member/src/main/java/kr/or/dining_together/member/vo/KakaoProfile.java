package kr.or.dining_together.member.vo;

import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoProfile {
	private Long id;
	private Kakao_properties kakao_properties;
	private Kakao_account kakao_account;


	@Getter
	@Setter
	@ToString
	public static class Kakao_properties {
		private String nickname;
		private String thumbnail_image;
		private String profile_image;
	}

	@Getter
	@Setter
	@ToString
	public static class Kakao_account {
		private String email;
		private String birthday;
		private String age_range;
		private String gender;
	}
}
