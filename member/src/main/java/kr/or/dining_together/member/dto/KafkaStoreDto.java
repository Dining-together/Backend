package kr.or.dining_together.member.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaStoreDto implements Serializable {
	private Schema schema;
	private Payload payload;
}
