package com.hms.billingms;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.paypal.base.rest.APIContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableRabbit
public class BillingmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingmsApplication.class, args);
	}
	
	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;
	
	@Value("${paypal.mode}")
	private String mode;

	@Bean
	public APIContext apiContext() {
		return new APIContext(clientId, clientSecret, mode);
	}
	
	@Bean
	public DirectExchange myExchange() {
		return new DirectExchange("hms-exchange");
	}

	@Bean
	public Queue myQueue() {
		return new Queue("biilings-queue");
	}

	@Bean
	public Binding myBinding(Queue myQueue, DirectExchange myExchange) {
		return BindingBuilder.bind(myQueue).to(myExchange).with("billings-key");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

}
