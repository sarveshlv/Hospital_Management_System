package com.Capg.Billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BillingModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingModuleApplication.class, args);
	}

}
