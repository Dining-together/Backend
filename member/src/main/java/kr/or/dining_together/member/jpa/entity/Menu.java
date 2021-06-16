package kr.or.dining_together.member.jpa.entity;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;

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
public class Menu{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long menuId;

	@Column(length = 100)
	@ApiModelProperty(notes = "메뉴 이름을 입력해 주세요")
	private String name;

	@Column(length = 100)
	private int price;

	@ApiModelProperty(value = "설명")
	private String description;

	@ManyToOne
	@JoinColumn(name = "id")
	private Store store;

}
