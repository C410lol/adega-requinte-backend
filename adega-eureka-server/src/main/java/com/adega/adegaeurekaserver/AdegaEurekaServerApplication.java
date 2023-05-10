package com.adega.adegaeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AdegaEurekaServerApplication{

	public static void main(String[] args) {
		SpringApplication.run(AdegaEurekaServerApplication.class, args);
	}

}
