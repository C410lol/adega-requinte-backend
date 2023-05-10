package com.adega.ms.user.adegauserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AdegaUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdegaUserServiceApplication.class, args);
	}

}
