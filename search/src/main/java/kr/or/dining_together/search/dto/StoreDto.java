package kr.or.dining_together.search.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "StoreDto", description = "가게")
@Builder
public class StoreDto implements Serializable {
	private String storeName;
	private String storeId;
	private String comment;
	private String phoneNum;
	private String storeType;
	private String addr;
	private double latitude;
	private double longitude;
	private String openTime;
	private String closedTime;
	private String storeImagePath;
}
