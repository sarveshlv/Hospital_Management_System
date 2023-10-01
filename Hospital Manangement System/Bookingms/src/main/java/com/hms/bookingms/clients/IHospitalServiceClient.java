package com.hms.bookingms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "HOSPITALMS", url = "http://localhost:8080/api/hospitals")
public interface IHospitalServiceClient {

    @GetMapping("/findById/{hospitalId}")
    ResponseEntity<?> isHospitalFound(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("hospitalId") String hospitalId);
}