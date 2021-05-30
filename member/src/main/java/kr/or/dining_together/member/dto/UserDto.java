package kr.or.dining_together.member.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.or.dining_together.member.jpa.entity.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "UserDto", description = "사용자")
public class UserDto {

	@ApiModelProperty(value = "사용자 pk")
	private long id;

	@ApiModelProperty(value = "아이디(이메일)")
	private String email;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "모바일 번호")
	private String phoneNo;

	@ApiModelProperty(value = "사용자 종류")
	private UserType userType;

	@ApiModelProperty(value = "사용자 권한", required = false)
	private List<String> roles = new ArrayList<>();
}
