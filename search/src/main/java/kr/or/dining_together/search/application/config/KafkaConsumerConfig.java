package kr.or.dining_together.search.application.config;

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

import kr.or.dining_together.search.dto.AuctionDto;
import kr.or.dining_together.search.dto.StoreDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${kafka.topic.general.id}")
	private String generalId;

	@Value(value = "${kafka.topic.auction.id}")
	private String auctionId;

	@Value(value = "${kafka.topic.store.id}")
	private String storeId;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, generalId);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	// 2. Consume auctionDto objects from Kafka
	@Bean
	public ConsumerFactory<String, AuctionDto> auctionConsumerFactory() {
		JsonDeserializer<AuctionDto> deserializer = new JsonDeserializer<>(AuctionDto.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, auctionId);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props,
			new StringDeserializer(),
			deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, AuctionDto> auctionKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, AuctionDto> factory
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(auctionConsumerFactory());
		return factory;
	}

	// 3. Consume storeDto objects from Kafka
	@Bean
	public ConsumerFactory<String, StoreDto> storeConsumerFactory() {
		JsonDeserializer<StoreDto> deserializer = new JsonDeserializer<>(StoreDto.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, storeId);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props,
			new StringDeserializer(),
			deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, StoreDto> storeKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, StoreDto> factory
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(storeConsumerFactory());
		return factory;
	}

}
