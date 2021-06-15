package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "customerFavorites")
@ApiModel(description = "사용자 즐겨찾기 객체")
public class CustomerFavorites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "userId")
	@ApiModelProperty(notes = "userId")
	private Long userId;

	@Column(name = "storeId")
	@ApiModelProperty(notes = "가게 Id")
	private Long storeId;
}
