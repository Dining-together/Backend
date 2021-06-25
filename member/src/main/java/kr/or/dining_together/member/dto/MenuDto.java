package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "MenuDto", description = "메뉴 불러오기")
public class MenuDto {

	@ApiModelProperty(value = "아이디(pkey)")
	private long menuId;

	@ApiModelProperty(value = "이름")
	private String name;

	@ApiModelProperty(value = "가격")
	private int price;

	@ApiModelProperty(value = "설명")
	private String description;

}
