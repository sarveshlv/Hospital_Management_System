package com.hms.hospitalms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableRabbit
public class HospitalmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalmsApplication.class, args);
	}
	
//	@Bean
//	public DirectExchange myExchange() {
//		return new DirectExchange("hms-exchange");
//	}
//
//	@Bean
//	public Queue myQueue() {
//		return new Queue("hospitals-queue");
//	}
//
//	@Bean
//	public Binding myBinding(Queue myQueue, DirectExchange myExchange) {
//		return BindingBuilder.bind(myQueue).to(myExchange).with("hospitals-key");
//	}
//
//	@Bean
//	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//		return new RabbitTemplate(connectionFactory);
//	}
}
