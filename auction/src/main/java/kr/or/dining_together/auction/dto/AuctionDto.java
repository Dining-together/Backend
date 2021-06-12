package kr.or.dining_together.auction.dto;

import java.util.Date;

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
@ApiModel(value = "AuctionDto", description = "공고")
@Builder
public class AuctionDto {

	@ApiModelProperty(value = "공고 pk")
	private String auctionId;

	@ApiModelProperty(notes = "공고 제목")
	private String title;

	@ApiModelProperty(notes = "공고 내용")
	private String content;

	@ApiModelProperty(notes = "공고 최대 가격")
	private String maxPrice;

	@ApiModelProperty(notes = "공고 최소 가격")
	private String minPrice;

	@ApiModelProperty(notes = "단체유형")
	private String userType;

	@ApiModelProperty(notes = "예약 시간")
	private Date reservation;

	@ApiModelProperty(notes = "공고 종료 시간")
	private Date deadline;

	@ApiModelProperty(notes = "사용자 id")
	private String userId;

}
