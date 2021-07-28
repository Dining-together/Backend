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
@ApiModel(value = "SuccessBidDto", description = "성공 공고")
@Builder
public class SuccessBidDto {

	@ApiModelProperty(notes = "AuctionId")
	private long auctionId;
	@ApiModelProperty(notes = "AuctioneerId")
	private long auctioneerId;
	@ApiModelProperty(notes = "userId")
	private long userId;
	@ApiModelProperty(notes = "인원수")
	private int groupCnt;
	@ApiModelProperty(notes = "단체유형")
	private String groupType;
	@ApiModelProperty(notes = "예약 시간")
	private Date reservation;
	@ApiModelProperty(notes = "업체 Name")
	private String storeName;

}
