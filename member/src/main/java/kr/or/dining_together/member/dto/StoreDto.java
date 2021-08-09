package kr.or.dining_together.member.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.or.dining_together.member.jpa.entity.StoreType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto implements Serializable {
	private String storeName;
	private String storeId;
	private String phoneNum;
	private String storeType;
	private String addr;
	private double latitude;
	private double longitude;
	private String openTime;
	private String closedTime;
	private String storeImagePath;
	private String comment;
}
