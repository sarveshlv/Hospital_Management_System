package com.Capg.Authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationModuleApplication.class, args);
    }

}
