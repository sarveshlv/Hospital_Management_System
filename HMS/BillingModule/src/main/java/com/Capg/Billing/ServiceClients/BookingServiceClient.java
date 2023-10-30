package com.Capg.Billing.ServiceClients;

import com.Capg.Billing.DTO.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "BOOKING-MICROSERVICE", url = "http://localhost:9090/Booking")
public interface BookingServiceClient {
    @PutMapping("/completeBooking/{bookingId}")
    Booking completeBooking(@RequestHeader("Authorization") String authorization, @PathVariable String bookingId);
}
