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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Field(type = FieldType.Text)
	private String comment;

	@Field(type = FieldType.Text)
	private String storeType;

	@Field(type = FieldType.Text)
	private String latitude;

	@Field(type = FieldType.Text)
	private String longitude;

	@Field(type = FieldType.Text)
	private String addr;

	@Field(type = FieldType.Text)
	private String phoneNum;

	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
	private LocalDateTime openTime;

	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
	private LocalDateTime closedTime;

	private String storeImagePath;

	public void update(String title,String phoneNum, String addr, String latitude, String longitude,
		String comment, String storeType, LocalDateTime openTime, LocalDateTime closedTime, String storeImagePath) {
		this.title=title;
		this.phoneNum = phoneNum;
		this.addr = addr;
		this.latitude = latitude;
		this.longitude = longitude;
		this.comment = comment;
		this.storeType = storeType;
		this.openTime = openTime;
		this.closedTime = closedTime;
		this.storeImagePath=storeImagePath;
	}
}
