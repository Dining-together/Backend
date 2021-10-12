package kr.or.dining_together.search.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"4. Trending"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search/trend")
public class ElasticAPIController {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static final String AUCTION_LOG_INDEX="auction_log";
	private static final String STORE_LOG_INDEX="store_log";
	private static final String RESULT_NAME="populars";

	private final ResponseService responseService;

	public RestHighLevelClient createConnection() {
		return new RestHighLevelClient(
			RestClient.builder(
				new HttpHost("49.50.160.149",9200,"http")
			)
		);
	}

	@GetMapping("/auctions")
	public ListResult<Long> getTrendingAuctions() throws IOException {
		String now=formatter.format(LocalDateTime.now());
		String sevenDaysFromNow= formatter.format(LocalDateTime.now().minusDays(7));
		List<Long> resultList = new ArrayList<>();
		try(
			RestHighLevelClient client = createConnection()
		) {
			AggregationBuilder aggregation=
				AggregationBuilders.terms(RESULT_NAME)
					.field("someField.auctionId")
					.order(BucketOrder.count(false))
					.size(5);

			RangeQueryBuilder rangeQuery= QueryBuilders
				.rangeQuery("@timestamp")
				.gte(sevenDaysFromNow)
				.lte(now);

			SearchResponse response=client.search(new SearchRequest(AUCTION_LOG_INDEX)
				.source(new SearchSourceBuilder()
					.size(0)
					.query(rangeQuery)
					.aggregation(aggregation)), RequestOptions.DEFAULT);

			log.debug("elasticsearch response: {} hits", response.getHits().getTotalHits());
			log.trace("elasticsearch response: {} hits", response);

			Terms terms= (Terms)response.getAggregations().getAsMap().get(RESULT_NAME);
			List<? extends Terms.Bucket> buckets=terms.getBuckets();

			for(Terms.Bucket bucket:buckets){
				Long key= (Long)bucket.getKey();
				if(key!=null){
					resultList.add(key);
				}
			}
			return responseService.getListResult(resultList);
		}
	}

	@GetMapping("/stores")
	public ListResult<Long> getTrendingStores() throws IOException {
		String now=formatter.format(LocalDateTime.now());
		String sevenDaysFromNow= formatter.format(LocalDateTime.now().minusDays(7));
		List<Long> resultList = new ArrayList<>();
		try(
			RestHighLevelClient client = createConnection()
		) {
			AggregationBuilder aggregation=
				AggregationBuilders.terms(RESULT_NAME)
					.field("someField.storeId")
					.order(BucketOrder.count(false))
					.size(5);

			RangeQueryBuilder rangeQuery= QueryBuilders
				.rangeQuery("@timestamp")
				.gte(sevenDaysFromNow)
				.lte(now);

			SearchResponse response=client.search(new SearchRequest(STORE_LOG_INDEX)
				.source(new SearchSourceBuilder()
					.size(0)
					.query(rangeQuery)
					.aggregation(aggregation)), RequestOptions.DEFAULT);

			log.debug("elasticsearch response: {} hits", response.getHits().getTotalHits());
			log.trace("elasticsearch response: {} hits", response);

			Terms terms= (Terms)response.getAggregations().getAsMap().get(RESULT_NAME);
			List<? extends Terms.Bucket> buckets=terms.getBuckets();

			for(Terms.Bucket bucket:buckets){
				Long key= (Long)bucket.getKey();
				if(key!=null){
					resultList.add(key);
				}
			}
			return responseService.getListResult(resultList);
		}
	}
}
