package com.hms.hospitalms;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
public class HospitalmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalmsApplication.class, args);
	}
	
	@Bean
	public DirectExchange myExchange() {
		return new DirectExchange("hms-exchange");
	}

	@Bean
	public Queue myQueue() {
		return new Queue("hospitals-queue");
	}

	@Bean
	public Binding myBinding(Queue myQueue, DirectExchange myExchange) {
		return BindingBuilder.bind(myQueue).to(myExchange).with("hospitals-key");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}
}
