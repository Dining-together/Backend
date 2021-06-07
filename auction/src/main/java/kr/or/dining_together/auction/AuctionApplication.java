package kr.or.dining_together.auction;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class AuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

}
