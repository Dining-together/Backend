package kr.or.dining_together.member.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import kr.or.dining_together.member.dto.ReviewScoreDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${kafka.topic.review.id}")
	private String reviewId;

	@Bean
	public ConsumerFactory<String, ReviewScoreDto> reviewConsumerFactory() {
		JsonDeserializer<ReviewScoreDto> deserializer = new JsonDeserializer<>(ReviewScoreDto.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, reviewId);

		return new DefaultKafkaConsumerFactory<>(props,
			new StringDeserializer(),
			deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ReviewScoreDto> reviewKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ReviewScoreDto> factory
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(reviewConsumerFactory());
		return factory;
	}

}
