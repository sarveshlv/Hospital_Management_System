package com.hms.bookingms.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hms.bookingms.dto.Bed;

import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "BEDMS", url = "http://localhost:8080/api/beds")
public interface IBedServiceClient {

    @PutMapping("/bookBed/{bedId}")
    ResponseEntity<Bed> bookBed(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("bedId") String bedId);

    @PutMapping("/unbookBed/{bedId}")
    ResponseEntity<Bed> unbookBed(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("bedId") String bedId);

    @GetMapping("/findByHospital/{hospitalId}")
    List<Bed> findBedsByHospitalId(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("hospitalId") String hospitalId);
    
    @PutMapping("/completeBed/{bedId}")
    ResponseEntity<Bed> completeBooking(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("bedId") String bedId);
}