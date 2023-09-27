package com.hms.billingms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hms.billingms.dto.Bed;

@FeignClient(name = "BEDMS", url = "http://localhost:8080/api/beds")
public interface IBedServiceClient {

    @PutMapping("/makeBedAvailable/{bedId}")
	Bed makeBedAvaialbe(@RequestHeader("Authorization") String authorizationHValue, String bedId);
}
