package kr.or.dining_together.search.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;

import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionTemplateService {
	private static final String AUCTION_INDEX = "auctions";

	private final ElasticsearchOperations elasticsearchOperations;

	private final RestHighLevelClient elasticsearchClient;

	public String scrollSearchAuction(String keyword) throws IOException {
		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
		SearchRequest searchRequest = new SearchRequest(AUCTION_INDEX);
		searchRequest.scroll(scroll);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(matchQuery("title", keyword));
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
		String scrollId = searchResponse.getScrollId();
		SearchHit[] searchHits = searchResponse.getHits().getHits();

		while (searchHits != null && searchHits.length > 0) {
			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
			scrollRequest.scroll(scroll);
			searchResponse = elasticsearchClient.scroll(scrollRequest, RequestOptions.DEFAULT);
			scrollId = searchResponse.getScrollId();
			searchHits = searchResponse.getHits().getHits();
		}

		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		ClearScrollResponse clearScrollResponse = elasticsearchClient.clearScroll(clearScrollRequest,
			RequestOptions.DEFAULT);
		boolean succeeded = clearScrollResponse.isSucceeded();

		return scrollId;
	}
}
