package kr.or.dining_together.auction.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RequestAuction {
	private String title;
	private String content;
	private String maxPrice;
	private String minPrice;
	private String userType;
	private Date reservation;
	private Date deadline;
}
