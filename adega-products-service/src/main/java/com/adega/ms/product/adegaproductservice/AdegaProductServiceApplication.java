package com.adega.ms.product.adegaproductservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AdegaProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdegaProductServiceApplication.class, args);
	}

}
