package kr.or.dining_together.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
@ApiModel(value = "UserIdDto", description = "사용자 아이디 (pkey)")
public class UserIdDto {

	@ApiModelProperty(value = "아이디")
	private long id;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value = "사용자 유형")
	private String type;
}