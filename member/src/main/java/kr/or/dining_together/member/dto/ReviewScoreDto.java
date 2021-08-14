package kr.or.dining_together.member.dto;

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
@ApiModel(value = "ReviewScoreDto", description = "리뷰 평균, 개수 전달 객체")
@Builder
public class ReviewScoreDto {
	@ApiModelProperty(value = "가게 ID")
	private Long storeId;

	@ApiModelProperty(value = "가게 리뷰수")
	private int reviewCnt;

	@ApiModelProperty(value = "리뷰 평균")
	private Double reviewAvg;
}
