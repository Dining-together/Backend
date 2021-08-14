package kr.or.dining_together.search.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreLogService {

	private final String indexName = "store_log";
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private final RestHighLevelClient elasticsearchClient;
	private String now = LocalDateTime.now().format(formatter);
	private String yesterday = LocalDateTime.now().minusDays(1).format(formatter);

	public String getViewCountPer30minute(String storeId) throws IOException {
		String rawQuery =
			"{" +
				"\"aggs\": {" +
				"\"0\": {" +
				"\"terms\": {" +
				"\"field\": \"someField.gender.keyword\"," +
				"\"order\": {" +
				"\"_count\": \"desc\"" +
				"}," +
				"\"size\": 5" +
				"}" +
				"}" +
				"}," +
				"\"size\": 0," +
				"\"fields\": [" +
				"{" +
				"\"field\": \"@timestamp\"," +
				"\"format\": \"date_time\"" +
				"}" +
				"]," +
				"\"script_fields\": {}," +
				"\"stored_fields\": [" +
				"\"*\"" +
				"]," +
				"\"runtime_mappings\": {}," +
				"\"_source\": {" +
				"\"excludes\": []" +
				"}," +
				"\"query\": {" +
				"\"bool\": {" +
				"\"must\": []," +
				"\"filter\": [" +
				"{" +
				"\"match_phrase\": {" +
				"\"someField.storeId\": " + storeId +
				"}" +
				"}," +
				"{" +
				"\"match_phrase\": {" +
				"\"someField.actionType.keyword\": \"view\"" +
				"}" +
				"}," +
				"{" +
				"\"range\": {" +
				"\"@timestamp\": {" +
				"\"gte\": " + yesterday + "," +
				"\"lte\": " + now + "," +
				"\"format\": \"strict_date_optional_time\"" +
				"}" +
				"}" +
				"}" +
				"]," +
				"\"should\": []," +
				"\"must_not\": []" +
				"}" +
				"}" +
				"}";

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.simpleQueryStringQuery(rawQuery));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(10);

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(indexName);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println(searchResponse);
		return "";
	}
}
