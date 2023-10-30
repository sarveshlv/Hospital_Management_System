package com.Capg.Billing.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BillingRequest {

    @NotEmpty(message = "Booking ID is required")
    private String bookingId;
}
