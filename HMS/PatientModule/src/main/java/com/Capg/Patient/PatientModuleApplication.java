package com.Capg.Patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PatientModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientModuleApplication.class, args);
	}

}
