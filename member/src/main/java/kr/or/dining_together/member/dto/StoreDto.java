package kr.or.dining_together.member.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto implements Serializable {
	private String storeId;
	private String storeName;
	private String addr;
	private String storeType;
	private String comment;
}
