package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "user")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "UserType", discriminatorType = DiscriminatorType.STRING)
public class User implements UserDetails {
	@Column(name = "createdDate")
	@ApiModelProperty(notes = "테이블의 생성일 정보입니다. 자동으로 입력됩니다.")
	public Date createdDate;
	@Column(name = "updatedDate")
	@ApiModelProperty(notes = "테이블의 수정일 정보입니다. 자동으로 입력됩니다.")
	public Date updatedDate;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Email
	@Column(nullable = false, unique = true, length = 100)
	private String email;

	// @Column(length = 100)
	// private String phoneNo;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(length = 100)
	@ApiModelProperty(notes = "패스워드을 입력해 주세요")
	private String password;
	@Column(length = 100)
	@Size(min = 2, message = "Name은 2글자 이상 입력.")
	@ApiModelProperty(notes = "사용자 이름을 입력해 주세요")
	private String name;
	@Past
	@ApiModelProperty(notes = "등록일 정보입니다. 자동으로 입력됩니다.")
	private Date joinDate;

	@Column(length = 100, columnDefinition = "varchar(100) default 'application'")
	private String provider;

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@PrePersist
	void joinDate() {
		this.joinDate = this.createdDate = this.updatedDate = new Date();
	}

	@PreUpdate
	void updateDate() {
		this.updatedDate = new Date();
	}

	//  json 출력 안함
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return this.email;
	}

	/**
	 * 아래는
	 * 로직 추가 작성 필요한 부분
	 */
	//  계정이 만료가 안됐는지
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//  계정이 잠기지 않았는지
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//  계정 패스워드가 만료 안됐는지
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//  계정 사용 가능한지
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}

	//수정용
	//public User update()

	public void updatePassword(String password) {
		this.password = password;
	}

}
