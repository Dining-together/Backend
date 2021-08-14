package kr.or.dining_together.auction.jpa.entity;

import java.time.LocalDateTime;
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
@Table(name = "auction")
@ApiModel(description = "공고 상세 정보를 위한 도메인 객체")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Auction {
	@Column(name = "createdDate")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public Date createdDate;

	@Column(name = "updatedDate")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public Date updatedDate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auctionId;

	@ApiModelProperty(notes = "공고 제목")
	private String title;

	@ApiModelProperty(notes = "공고 내용")
	@Column(length = 500)
	private String content;

	@ApiModelProperty(notes = "공고 최대 가격")
	private int maxPrice;

	@ApiModelProperty(notes = "공고 최소 가격")
	private int minPrice;

	@ApiModelProperty(notes = "인원수")
	private int groupCnt;

	@ApiModelProperty(notes = "도로명 주소")
	private String addr;

	@ApiModelProperty(notes = "마감여부")
	private AuctionStatus status;

	@ApiModelProperty(notes = "단체유형")
	private String groupType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@ApiModelProperty(notes = "예약 시간")
	private LocalDateTime reservation;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@ApiModelProperty(notes = "공고 종료 시간")
	private LocalDateTime deadline;

	@ApiModelProperty(notes = "사용자 id")
	private long userId;

	@ApiModelProperty(notes = "사용자 Name")
	private String userName;

	@ApiModelProperty(value = "사용자 이미지 경로")
	private String path;

	@ApiModelProperty(notes = "선호 업체")
	private String storeType;

	@ApiModelProperty(notes = "평균 성별")
	private String gender;

	@ApiModelProperty(notes = "평균 나이")
	private String age;

	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
	private List<Auctioneer> auctioneers = new ArrayList<>();

	@PrePersist
	void prePersist() {
		this.createdDate = this.updatedDate = new Date();
		this.status = AuctionStatus.PROCEEDING;
	}

	@PreUpdate
	void updateDate() {
		this.updatedDate = new Date();
	}

	public void setStatus(AuctionStatus status) {
		this.status = status;
	}

	public void setUpdate(String title, String content, String storeType, int minPrice, String groupType, int groupCnt,
		int maxPrice,
		LocalDateTime reservation, LocalDateTime deadline) {
		this.title = title;
		this.content = content;
		this.storeType = storeType;
		this.minPrice = minPrice;
		this.groupType = groupType;
		this.groupCnt = groupCnt;
		this.maxPrice = maxPrice;
		this.reservation = reservation;
		this.deadline = deadline;
	}

}
