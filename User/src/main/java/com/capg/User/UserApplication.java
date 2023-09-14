package com.capg.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ComponentScan({"com.*"})
@EnableMongoRepositories({"com.*"})
@EnableMethodSecurity(prePostEnabled = true)
public class UserApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserApplication.class, args);
	}

}
