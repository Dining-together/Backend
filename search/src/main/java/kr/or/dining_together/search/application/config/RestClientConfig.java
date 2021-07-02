package kr.or.dining_together.search.application.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class RestClientConfig extends AbstractElasticsearchConfiguration {

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
			.connectedTo("118.67.133.150:9200")
			.build();

		return RestClients.create(clientConfiguration).rest();
	}
}
