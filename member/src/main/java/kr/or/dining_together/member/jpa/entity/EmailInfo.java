package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "email")
@ApiModel(description = "이메일 인증을 위한 도메인 객체")
public class EmailInfo {
	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Email
	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(length = 100)
	@ApiModelProperty(notes = "이메일 인증키")
	private String key;

	@Column(nullable = false)
	@ApiModelProperty(notes = "인증 여부")
	private Boolean used;
}
