package kr.or.dining_together.member.vo;

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
public class CustomerProfileRequest {

	private String password;
	private String name;
	private int age;
	private String gender;
	private double latitude;
	private double longitude;
	private String addr;

}
