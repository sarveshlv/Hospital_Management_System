package com.Capg.BedModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BedModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BedModuleApplication.class, args);
	}

}
