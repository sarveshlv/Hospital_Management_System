package com.Capg.Booking.FeignClients;

import com.Capg.Booking.DTO.Bed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "BED-MICROSERVICE", url = "http://localhost:9090/Bed")
public interface BedServiceClient {

    @PutMapping("/bookBed/{bedId}")
    ResponseEntity<Bed> bookBed(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("bedId") String bedId);

    @GetMapping("/findByType/{bedType}")
    List<Bed> findBedsByType(@RequestHeader("Authorization") String authorizationHValue, @PathVariable("bedType") String bedType);
}
