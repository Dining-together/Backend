package kr.or.dining_together.auction.vo;

import java.time.LocalDateTime;

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
public class AuctionRequest {
	private String title;
	private String content;
	private String storeType;
	private int maxPrice;
	private int minPrice;
	private String groupType;
	private int groupCnt;
	private LocalDateTime reservation;
	private LocalDateTime deadline;
	private String age;
	private String gender;
	private String addr;
}
