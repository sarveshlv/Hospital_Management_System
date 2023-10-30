package com.Capg.Hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HospitalModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalModuleApplication.class, args);
	}

}
