package kr.or.dining_together.member.vo;

import java.util.Date;
import java.util.List;

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
public class FacilityRequest {
	private int capacity;

	private int parkingCount;

	private boolean parking;

	private Date openTime;

	private Date closedTime;

	private List<String> FacilityEtcNames;

}
