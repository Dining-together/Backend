package kr.or.dining_together.auction.jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "successBid")
@ApiModel(description = "공고 상세 정보를 위한 도메인 객체")
public class SuccessBid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long successBidId;
	@ApiModelProperty(notes = "AuctionId")
	private long auctionId;
	@ApiModelProperty(notes = "AuctioneerId (storeId)")
	private long auctioneerId;
	@ApiModelProperty(notes = "userId")
	private long userId;

	@ApiModelProperty(notes = "완료")
	private boolean isComplete;
	@ApiModelProperty(notes = "인원수")
	private int groupCnt;
	@ApiModelProperty(notes = "단체유형")
	private String groupType;
	@ApiModelProperty(notes = "예약 시간")
	private Date reservation;
	@ApiModelProperty(notes = "업체 Name")
	private String storeName;

	@PrePersist
	void prePersist() {
		this.isComplete = false;
	}

}
