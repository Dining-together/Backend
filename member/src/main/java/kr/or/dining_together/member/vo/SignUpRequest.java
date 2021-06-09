package kr.or.dining_together.member.vo;

import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.entity.UserType;
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
public class SignUpRequest {
	private UserDto userDto;
	private UserType userType;
	private int age;
	private String gender;
}
