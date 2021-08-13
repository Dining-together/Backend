package kr.or.dining_together.auction.jpa.entity;

import java.time.LocalDateTime;

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
	@ApiModelProperty(notes = "storeId")
	private long storeId;
	@ApiModelProperty(notes = "userId")
	private long userId;
	@ApiModelProperty(notes = "예상 가격")
	private int price;
	@ApiModelProperty(notes = "업체 메뉴")
	private String menu;
	@ApiModelProperty(notes = "완료")
	private boolean isComplete;
	@ApiModelProperty(notes = "인원수")
	private int groupCnt;
	@ApiModelProperty(notes = "단체유형")
	private String groupType;
	@ApiModelProperty(notes = "예약 시간")
	private LocalDateTime reservation;
	@ApiModelProperty(notes = "업체 Name")
	private String storeName;
	@ApiModelProperty(notes = "리뷰여부")
	private boolean isReview;
	@PrePersist
	void prePersist() {
		this.isComplete = false;
		this.isReview=false;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public void setReview(boolean isReview) {
		this.isReview = isReview;
	}

}
