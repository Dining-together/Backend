package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CustomerDto", description = "사용자")
@Builder
public class CustomerDto {
	@ApiModelProperty(value = "아이디(이메일)")
	private String email;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "성별")
	private String gender;

	@ApiModelProperty(value = "나이")
	private int age;

	@ApiModelProperty(value = "전화번호")
	private String phoneNum;
}

