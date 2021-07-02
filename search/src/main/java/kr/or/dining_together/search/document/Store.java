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
	String id;

	@Field(type = FieldType.Text)
	String title;

	@Field(type = FieldType.Text)
	String storeType;

	@Field(type = FieldType.Text)
	String location;
}
