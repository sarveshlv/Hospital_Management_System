package com.hms.bookingms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PATIENTMS", url = "http://localhost:8080/api/patients")
public interface IPatientServiceClient {

    @GetMapping("/findById/{patientId}")
    ResponseEntity<?> isPatientFound(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("patientId") String patientId);
}