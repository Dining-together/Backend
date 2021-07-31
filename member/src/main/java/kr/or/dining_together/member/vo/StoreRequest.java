package kr.or.dining_together.member.vo;

import java.util.Date;

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
	private String phoneNum;
	private StoreType storeType;
	private String addr;
	private double latitude;
	private double longitude;
	private Date openTime;
	private Date closedTime;

}
