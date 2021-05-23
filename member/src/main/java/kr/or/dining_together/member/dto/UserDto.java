package kr.or.dining_together.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "UserDto", description = "사용자")
public class UserDto {
    @ApiModelProperty(value = "사용자 pk")
    private long id;
    @ApiModelProperty(value = "아이디(이메일)")
    private String email;
    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "모바일 번호")
    private String phoneNo;
    @ApiModelProperty(value = "사용자 권한", required = false)
    private List<String> roles = new ArrayList<>();

}
