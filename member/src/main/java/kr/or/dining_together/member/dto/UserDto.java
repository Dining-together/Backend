package kr.or.dining_together.member.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserDto", description = "사용자")
@Builder
public class UserDto {
	@ApiModelProperty(value = "아이디(이메일)")
	private String email;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "사용자 권한", required = false)
	private List<String> roles = new ArrayList<>();
}
