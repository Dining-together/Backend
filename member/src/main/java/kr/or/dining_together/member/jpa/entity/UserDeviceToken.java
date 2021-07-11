package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "userDeviceToken")
@ApiModel(description = "사용자 FCM 토큰정보 관리")
public class UserDeviceToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(value = "설명")
	private String token;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User user;
}
