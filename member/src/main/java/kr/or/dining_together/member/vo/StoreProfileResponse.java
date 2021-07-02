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
public class StoreProfileResponse {

	private String email;
	private String password;
	private String name;
	private String phoneNum;

}
