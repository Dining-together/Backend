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
@ApiModel(value = "AuctioneerDto", description = "공고 참여 업체")
@Builder
public class AuctioneerDto {
	@ApiModelProperty(notes = "참여업체 id")
	private Long auctioneerId;

	@ApiModelProperty(notes = "업체 id")
	private long storeId;

	private String storeName;

	@ApiModelProperty(notes = "업체 메뉴")
	private String menu;

	@ApiModelProperty(notes = "예상 가격")
	private int price;

	@ApiModelProperty(notes = "업체 소개")
	private String content;
}
