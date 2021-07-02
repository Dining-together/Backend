package kr.or.dining_together.search.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "auctions")
public class Auction {
	@Id
	String id;

	@Field(type = FieldType.Text, name = "title")
	String title;

	@Field(type = FieldType.Text, name = "location")
	String location;

	@Field(type = FieldType.Text, name = "menu")
	String menu;

	@Field(type = FieldType.Integer, name = "maxPrice")
	Integer maxPrice;

	@Field(type = FieldType.Integer, name = "minPrice")
	Integer minPrice;

	@Field(type = FieldType.Date, name = "createdDate")
	LocalDateTime createdDate;
}
