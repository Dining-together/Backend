package kr.or.dining_together.auction.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "reviewImages")
@ApiModel(description = "리뷰 사진")
public class ReviewImages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "path")
	@ApiModelProperty(notes = "경로")
	private String path;

	@Column(name = "file_name")
	@ApiModelProperty(notes = "파일명")
	private String file_name;

	@ManyToOne
	@JoinColumn(name = "reviewId")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Review review;

}
