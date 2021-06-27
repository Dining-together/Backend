package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long menuId;

	@Column(length = 100)
	@ApiModelProperty(notes = "메뉴 이름을 입력해 주세요")
	private String name;

	@ApiModelProperty(notes = "메뉴 사진 경로")
	private String path;

	@Column(length = 100)
	private int price;

	@ApiModelProperty(value = "설명")
	private String description;

	@ManyToOne
	@JoinColumn(name = "id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Store store;

	public void update(String name, String path, int price, String description) {
		this.name = name;
		this.path = path;
		this.price = price;
		this.description = description;
	}
}
