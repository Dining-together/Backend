package kr.or.dining_together.member.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreDto implements Serializable {
	private String storeId;
	private String storeName;
	private String addr;
	private String storeType;
}
