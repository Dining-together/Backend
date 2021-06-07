package kr.or.dining_together.auction.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import io.swagger.annotations.Api;
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
@Table(name = "auction")
@ApiModel(description = "공고 상세 정보를 위한 도메인 객체")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auctionId;
	@ApiModelProperty(notes = "공고 제목")
	private String title;
	@ApiModelProperty(notes = "공고 내용")
	@Column(length = 500)
	private String content;
	@ApiModelProperty(notes = "공고 최대 가격")
	private String max_price;
	@ApiModelProperty(notes = "공고 최소 가격")
	private String min_price;
	@ApiModelProperty(notes = "공고 상태")
	private Status status;
	@ApiModelProperty(notes = "단체유형")
	private String userType;
	@ApiModelProperty(notes = "예약 시간")
	private Date reservation;
	@ApiModelProperty(notes = "공고 종료 시간")
	private Date deadline;


	@Column(name = "createdDate")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public Date createdDate;
	@Column(name = "updatedDate")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public Date updatedDate;

	@PrePersist
	void joinDate() {
		this.createdDate = this.updatedDate = new Date();
	}

	@PreUpdate
	void updateDate() {
		this.updatedDate = new Date();
	}
	public void setUpdate(String title, String content, String min_price, String userType,String max_price,Date reservation,Date deadline){
		this.title=title;
		this.content=content;
		this.min_price=min_price;
		this.userType=userType;
		this.max_price=max_price;
		this.reservation=reservation;
		this.deadline=deadline;
	}


}
