package kr.or.dining_together.member.jpa.entity;
/**
 * @package : kr.or.dining_together.member.jpa.entity
 * @name : Favorites.java
 * @date : 2021-06-07 오후 9:27
 * @author : qja96
 * @version : 1.0.0
 * @modifyed :
 * @description : 즐겨찾기 정보 저장.
 **/

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
@Table(name = "favorites")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
public class Favorites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "userId")
	@ApiModelProperty(notes = "userId")
	private long userId;

	@Column(name = "auctionId")
	@ApiModelProperty(notes = "경매 Id")
	private long auctionId;

	@Column(name = "storeId")
	@ApiModelProperty(notes = "가게 Id")
	private long storeId;
}
