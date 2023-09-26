package com.Capg.Billing.ServiceClients;

import com.Capg.Billing.DTO.Bed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "BED-MICROSERVICE", url = "http://localhost:9090/Bed")
public interface BedServiceClient {
    @PutMapping("/makeBedAvailable/{bedId}")
    Bed makeBedAvailable(@RequestHeader("Authorization") String authorizationHValue, @PathVariable String bedId);
}
