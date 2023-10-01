package com.hms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RouterGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouterGatewayApplication.class, args);
	}

//	@Bean
//	public CorsFilter corsFilter() {
//		org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
//		corsConfiguration.setAllowCredentials(true);
//		corsConfiguration.addAllowedOrigin("*");
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
//		corsConfiguration.addAllowedHeader("origin");
//		corsConfiguration.addAllowedHeader("content-type");
//		corsConfiguration.addAllowedHeader("accept");
//		corsConfiguration.addAllowedHeader("authorization");
//		corsConfiguration.addAllowedHeader("cookie");
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfiguration);
//		return new CorsFilter(source);
//	}
//	
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//	    final CorsConfiguration config = new CorsConfiguration();
//
//	    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//	    config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//	    config.setAllowCredentials(true);
//	    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//
//	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", config);
//
//	    return source;
//	}

}