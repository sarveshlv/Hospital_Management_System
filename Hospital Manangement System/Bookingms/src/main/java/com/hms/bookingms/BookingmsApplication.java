package com.hms.bookingms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EnableRabbit
public class BookingmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingmsApplication.class, args);
	}
	
//	@Bean
//	public DirectExchange myExchange() {
//		return new DirectExchange("hms-exchange");
//	}
//
//	@Bean
//	public Queue myQueue() {
//		return new Queue("bookings-queue");
//	}
//
//	@Bean
//	public Binding myBinding(Queue myQueue, DirectExchange myExchange) {
//		return BindingBuilder.bind(myQueue).to(myExchange).with("bookings-key");
//	}
//
//	@Bean
//	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//		return new RabbitTemplate(connectionFactory);
//	}
}