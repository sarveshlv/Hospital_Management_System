package com.Capg.BedModule.FeignClients;

import com.Capg.BedModule.DTO.Hospital;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "HOSPITAL-MICROSERVICE", url = "http://localhost:9090/Hospital")
public interface HospitalServiceClient {

    @GetMapping("/getHospital/{hospitalId}")
    ResponseEntity<?> isHospitalFound(@RequestHeader("Authorization") String authorizationValue, @PathVariable("hospitalId") String hospitalId);

    @GetMapping("/findNearbyHospitals/{pincode}")
    List<Hospital> getNearbyHospitals(@RequestHeader("Authorization") String authorizationValue, @PathVariable("pincode") Long pincode);
}
