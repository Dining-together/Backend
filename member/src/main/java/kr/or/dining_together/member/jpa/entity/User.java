package kr.or.dining_together.member.jpa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="user")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
public class User implements UserDetails {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 100)
    @ApiModelProperty(notes = "패스워드을 입력해 주세요")
    private String password;
    @Column(length = 100)
    @Size(min=2,message="Name은 2글자 이상 입력.")
    @ApiModelProperty(notes = "사용자 이름을 입력해 주세요")
    private String name;
    @Column(length = 100)
    private String phoneNo;
    @Past
    @ApiModelProperty(notes = "등록일을 입력해 주세요")
    private Date joinDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
//  json 출력 안함
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

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
}
