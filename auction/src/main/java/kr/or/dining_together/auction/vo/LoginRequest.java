package kr.or.dining_together.auction.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@NotNull(message = "이메일은 빈칸일수 없습니다.")
	@Email
	private String email;
	@NotNull(message = "비밀번호는 빈칸일수 없습니다.")
	private String password;
}
