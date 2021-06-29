package kr.or.dining_together.auction.vo;

import java.util.Date;

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
public class RequestAuction {
	private String title;
	private String content;
	private int maxPrice;
	private int minPrice;
	private String userType;
	private Date reservation;
	private Date deadline;
}