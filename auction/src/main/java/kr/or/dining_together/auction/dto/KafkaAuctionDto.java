package kr.or.dining_together.auction.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaAuctionDto implements Serializable {
	private Schema schema;
	private Payload payload;
}
