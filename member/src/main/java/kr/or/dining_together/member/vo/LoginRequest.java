package kr.or.dining_together.member.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginRequest {
	@NotNull(message = "이메일은 빈칸일수 없습니다.")
	@Email
	private String email;
	@NotNull(message = "비밀번호는 빈칸일수 없습니다.")
	private String password;
}
