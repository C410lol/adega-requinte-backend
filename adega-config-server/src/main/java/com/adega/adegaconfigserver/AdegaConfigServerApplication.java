package com.adega.adegaconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class AdegaConfigServerApplication{

	public static void main(String[] args) {
		SpringApplication.run(AdegaConfigServerApplication.class, args);
	}

}
