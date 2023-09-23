package com.hms.billingms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hms.billingms.dto.Booking;

@FeignClient(name = "BOOKINGMS", url = "http://localhost:8080/api/bookings")
public interface IBookingServiceClient {
	
	@GetMapping("/find/{bookingId}")
	Booking completeBooking(@RequestHeader("Authorization") String authorizationHValue, @PathVariable String bookingId);
}
