package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@ApiModel(value = "UserIdDto", description = "사용자 id & 이름 (다른 서비스 호출)")
public class UserIdDto {

	@ApiModelProperty(value = "아이디(pkey)")
	private long id;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value="이메일")
	private String email;

	@ApiModelProperty(value = "사용자 이미지 경로")
	private String path;

	@ApiModelProperty(value = "사용자 유형")
	private String type;
}
