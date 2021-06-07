package kr.or.dining_together.member.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@DiscriminatorValue(value = UserType.Values.STORE)
@PrimaryKeyJoinColumn(name = "user_id")
@SuperBuilder
@NoArgsConstructor
public class Store extends User {
	@ApiModelProperty(notes = "서류 제출 확인여부를 나타낸다")
	private Boolean documentChecked;

	// @Builder
	// public Store(String email, String password,
	// 	String name,
	// 	List<String> roles,
	// 	Boolean documentChecked) {
	// 	super(email, password, name, roles);
	// 	this.documentChecked = documentChecked;
	// }
}
