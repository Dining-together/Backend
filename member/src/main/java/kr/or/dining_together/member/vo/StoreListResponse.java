package kr.or.dining_together.member.vo;

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
public class StoreListResponse {
	private long id;
	private String name;
	private String addr;
	private String path;
	private String comment;
	private double latitude;
	private double longitude;
	private StoreType storeType;

}
