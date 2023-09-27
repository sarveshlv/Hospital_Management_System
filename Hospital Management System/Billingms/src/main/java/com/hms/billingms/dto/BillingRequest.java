package com.hms.billingms.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BillingRequest {
	
	@NotEmpty(message="Booking id is required")
	private String bookingId;
}
