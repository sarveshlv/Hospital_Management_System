package com.hms.billingms.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "billings")
public class Billing {
	private String id;
	@Indexed(unique = true)
	private String bookingId;
	private Double billAmount;
	private PaymentStatus paymentStatus;

	public enum PaymentStatus {
		PENDING, COMPLETED
	}
}