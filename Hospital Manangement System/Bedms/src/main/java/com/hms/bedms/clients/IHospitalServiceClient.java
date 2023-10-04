package com.hms.bedms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hms.bedms.dtos.Hospital;

import java.util.List;

@FeignClient(name = "HOSPITALMS", url = "http://localhost:8080/api/hospitals")
public interface IHospitalServiceClient {

    @GetMapping("/findById/{hospitalId}")
    ResponseEntity<?> isHospitalFound(@RequestHeader("Authorization") String authorizationValue, @PathVariable("hospitalId") String hospitalId);

    @GetMapping("/nearby/{pincode}")
    List<Hospital> getNearbyHospitals(@RequestHeader("Authorization") String authorizationValue, @PathVariable("pincode") Long pincode);
}