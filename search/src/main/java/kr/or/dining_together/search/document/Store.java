package kr.or.dining_together.search.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "stores")
public class Store {
	@Id
	private String id;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text, name = "description")
	private String description;

	@Field(type = FieldType.Text)
	private String storeType;

	@Field(type = FieldType.Text)
	private String Latitude;

	@Field(type = FieldType.Text)
	private String Longitude;

	@Field(type = FieldType.Boolean)
	private Boolean bookmark;

	@Field(type = FieldType.Text)
	private String reviewScore;
}
