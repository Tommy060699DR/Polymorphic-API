package com.allobank.idr_rate_aggregator;

import com.allobank.idr_rate_aggregator.config.FrankfurterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class IdrRateAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrRateAggregatorApplication.class, args);
	}

}
