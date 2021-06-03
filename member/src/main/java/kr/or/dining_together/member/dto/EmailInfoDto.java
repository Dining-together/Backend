package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "EmailInfoDto", description = "이메일 인증정보 저장")
public class EmailInfoDto {

	@ApiModelProperty(value = "이메일 pk")
	private long id;

	@ApiModelProperty(value = "아이디(이메일)")
	private String email;

	@ApiModelProperty(value = "이름")
	private String key;

	@ApiModelProperty(value = "인증여부")
	private Boolean used;
}
