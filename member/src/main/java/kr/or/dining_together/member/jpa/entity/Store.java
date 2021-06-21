package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
	@Column(columnDefinition = "boolean default false")
	private Boolean documentChecked;

	@OneToMany(mappedBy = "store")
	private List<Menu> menus = new ArrayList<>();

}
