package kr.or.dining_together.auction.jpa.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table(name = "review")
@ApiModel(description = "리뷰")
public class Review {

	@Column(name = "createdDate")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public Date createdDate;
	@Column(name = "updatedDate")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public Date updatedDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	@ApiModelProperty(notes = "내용")
	private String content;
	@ApiModelProperty(notes = "별점")
	private int score;
	@ApiModelProperty(notes = "사용자 id")
	private long userId;
	@ApiModelProperty(notes = "사용자 Name")
	private String userName;
	@ApiModelProperty(notes = "업체 id")
	private long storeId;
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
	private List<ReviewImages> reviewImages = new ArrayList<>();

	@PrePersist
	void prePersist() {
		this.createdDate = this.updatedDate = new Date();
	}

	@PreUpdate
	void updateDate() {
		this.updatedDate = new Date();
	}

	public void updateReview(String content, int score) {
		this.content = content;
		this.score = score;
	}

}
