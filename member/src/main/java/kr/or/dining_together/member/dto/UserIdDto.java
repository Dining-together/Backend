package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "UserIdDto", description = "사용자 id & 이름 (다른 서비스 호출)")
public class UserIdDto {

	@ApiModelProperty(value = "아이디(pkey)")
	private long id;

	@ApiModelProperty(value = "이름")
	private String name;
}
