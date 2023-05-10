package com.adega.ms.mail.adegamailsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AdegaMailsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdegaMailsServiceApplication.class, args);
	}

}
