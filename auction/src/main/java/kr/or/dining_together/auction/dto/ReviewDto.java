package kr.or.dining_together.auction.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ReviewDto", description = "리뷰")
@Builder
public class ReviewDto {

	@ApiModelProperty(notes = "내용")
	private String content;
	@ApiModelProperty(notes = "별점")
	private int score;
}
