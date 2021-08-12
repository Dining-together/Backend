package kr.or.dining_together.member.vo;

import java.time.LocalDateTime;

import kr.or.dining_together.member.jpa.entity.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {
	private String storeName;
	private String phoneNum;
	private StoreType storeType;
	private String addr;
	private String comment;
	private double latitude;
	private double longitude;
	private LocalDateTime openTime;
	private LocalDateTime closedTime;
}
