package kr.or.dining_together.search.application.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class TopicConfig {
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${kafka.topic.general.name}")
	private String generalTopicName;

	@Value(value = "${kafka.topic.auction.name}")
	private String auctionTopicName;

	@Value(value = "${kafka.topic.store.name}")
	private String storeTopicName;

	@Bean
	public NewTopic generalTopic() {
		return TopicBuilder.name(generalTopicName)
			.partitions(1)
			.replicas(1)
			.build();
	}

	@Bean
	public NewTopic auctionTopic() {
		return TopicBuilder.name(auctionTopicName)
			.partitions(1)
			.replicas(1)
			.build();
	}

	@Bean
	public NewTopic storeTopic() {
		return TopicBuilder.name(storeTopicName)
			.partitions(1)
			.replicas(1)
			.build();
	}

	//If not using spring boot

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}
}
