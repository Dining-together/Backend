package kr.or.dining_together.member.vo;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

	@NotEmpty
	private String oldPassword;

	@NotEmpty
	private String newPassword;

}
