package com.hms.hospitalms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HospitalmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalmsApplication.class, args);
	}

}
