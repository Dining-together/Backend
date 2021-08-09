package kr.or.dining_together.search.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @package : kr.or.dining_together.search.document
 * @name : Auction.java
 * @date : 2021-07-07 오후 6:47
 * @author : qja96
 * @version : 1.0.0
 * @modifyed :
 * @description :
 * 프론트 & erd 참고하여 구성
 * 제목, 게시일, 단체유형, 선호 지역, 선호 메뉴, 선호 가격대
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "auctions")
public class Auction {
	@Id
	private String id;

	@Field(type = FieldType.Text, name = "title")
	private String title;

	@Field(type = FieldType.Text, name = "title")
	private String content;

	@Field(type = FieldType.Text, name = "userName")
	private String userName;

	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
	private LocalDateTime reservation;

	@Field(type = FieldType.Text, name = "userType")
	private String userType;

	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
	private LocalDateTime deadLine;

	@Field(type = FieldType.Integer, name = "maxPrice")
	private int maxPrice;

	@Field(type = FieldType.Integer, name = "minPrice")
	private int minPrice;

	@Field(type = FieldType.Text, name = "storeType")
	private String storeType;

}
