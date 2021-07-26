package kr.or.dining_together.auction.jpa.entity;

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

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Auctioneer {
	@Column(name = "createdDate")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public Date createdDate;
	@Column(name = "updatedDate")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public Date updatedDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auctioneerId;
	@ManyToOne
	@JoinColumn(name = "auctionId")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

	@PrePersist
	void prePersist() {
		this.createdDate = this.updatedDate = new Date();
	}

	@PreUpdate
	void updateDate() {
		this.updatedDate = new Date();
	}

	public void update(String content, String menu, int price) {
		this.content = content;
		this.menu = menu;
		this.price = price;
	}

}
