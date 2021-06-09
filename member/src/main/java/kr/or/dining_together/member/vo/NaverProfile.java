package kr.or.dining_together.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverProfile {
	private String resultcode;
	private String message;
	private Response response;

	@Getter
	@Setter
	@ToString
	public static class Response {
		private String id;
		private String name;
		private String email;

	}
}
