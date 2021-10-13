package kr.or.dining_together.auction.jpa.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "auctioneer")
@ApiModel(description = "공고 참여 업체")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Auctioneer {
	@Column(name = "createdDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public LocalDateTime createdDate;
	@Column(name = "updatedDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public LocalDateTime updatedDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auctioneerId;
	@ManyToOne
	@JoinColumn(name = "auctionId")
	private Auction auction;
	@ApiModelProperty(notes = "업체 id")
	private long storeId;
	@ApiModelProperty(notes = "업체 Name")
	private String storeName;
	@ApiModelProperty(notes = "업체 메뉴")
	private String menu;
	@ApiModelProperty(notes = "예상 가격")
	private int price;
	@ApiModelProperty(notes = "업체 소개")
	private String content;
	@ApiModelProperty(notes = "낙찰여부")
	private boolean success;
	@ApiModelProperty(notes = "업체 이미지 경로")
	private String path;

	@PrePersist
	void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdDate = this.updatedDate = now;
		this.success=false;
	}

	@PreUpdate
	void updateDate() {
		LocalDateTime now = LocalDateTime.now();
		this.updatedDate = now;
	}

	public void update(String content, String menu, int price) {
		this.content = content;
		this.menu = menu;
		this.price = price;
	}

	public void setSuccess(boolean success){
		this.success=success;
	}

}
