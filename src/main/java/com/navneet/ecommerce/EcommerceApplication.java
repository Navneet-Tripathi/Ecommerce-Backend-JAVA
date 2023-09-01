package com.navneet.ecommerce;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableRetry
public class EcommerceApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EcommerceApplication.class, args);
		/*
		 * RaceCondition raceCondition = context.getBean(RaceCondition.class);
		 * raceCondition.run();
		 */
	}

	@Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
