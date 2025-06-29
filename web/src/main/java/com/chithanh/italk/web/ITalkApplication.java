package com.chithanh.italk.web;

import com.chithanh.italk.security.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@ConfigurationPropertiesScan(basePackages = "com.chithanh.italk")
@ComponentScan(basePackages = {"com.chithanh.italk.*"})
@EntityScan(basePackages = {"com.chithanh.italk.*"})
@EnableJpaRepositories(basePackages = {"com.chithanh.italk.*"})
@EnableScheduling
public class ITalkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ITalkApplication.class, args);
    }
}