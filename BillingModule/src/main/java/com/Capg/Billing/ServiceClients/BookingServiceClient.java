package com.Capg.Billing.ServiceClients;

import com.Capg.Billing.DTO.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-MICROSERVICE", url = "http://localhost:9090/Booking")
public interface BookingServiceClient {
//    @GetMapping("")
//    Booking completeBooking(@PathVariable String bookingId);
}
