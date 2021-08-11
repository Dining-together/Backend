package kr.or.dining_together.search.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
@ApiModel(value = "AuctionDto", description = "공고")
@Builder
public class AuctionDto implements Serializable {
	private String auctionId;

	@ApiModelProperty(notes = "공고 제목")
	private String title;

	@ApiModelProperty(notes = "공고 내용")
	private String content;

	@ApiModelProperty(notes = "공고 최대 가격")
	private int maxPrice;

	@ApiModelProperty(notes = "공고 최소 가격")
	private int minPrice;

	@ApiModelProperty(notes = "단체유형")
	private String userType;

	@ApiModelProperty(notes = "예약 시간")
	private String reservation;

	@ApiModelProperty(notes = "공고 종료 시간")
	private String deadline;

	// @ApiModelProperty(notes = "사용자 id")
	// private String userId;

	@ApiModelProperty(notes = "사용자 Name")
	private String userName;

	@ApiModelProperty(notes="선호 가게종류")
	private String storeType;

}
