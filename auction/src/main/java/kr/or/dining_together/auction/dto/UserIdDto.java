package kr.or.dining_together.auction.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "UserIdDto", description = "사용자 아이디 (pkey)")
public class UserIdDto {

	@ApiModelProperty(value = "아이디")
	private String id;

	@ApiModelProperty(value = "이름")
	private String name;
}