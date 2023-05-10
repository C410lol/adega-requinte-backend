package com.adega.ms.order.adegaorderservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfig {

    private final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER =
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    @Bean
    public LocalDateTime localDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        var timeModule = new JavaTimeModule();
        timeModule.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper().registerModule(timeModule);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
