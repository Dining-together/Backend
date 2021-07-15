package kr.or.dining_together.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
	private String id;
	private String storeName;
	private String addr;
	private String storeType;
}
