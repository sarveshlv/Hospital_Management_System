package com.Capg.BedModule.Controller;

import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Exceptions.BedNotFoundException;
import com.Capg.BedModule.Exceptions.BedStatusInvalidException;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Service.BedService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bed")
public class BedController {

    @Autowired
    private BedService bedService;


    //Register a bed using AddBedDTO, returns a Bed object if successful
    @PostMapping("/register")
    public ResponseEntity<Bed> registerBed(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody AddBedDTO addBedObj) {
        return ResponseEntity.ok().body(bedService.registerBed(authorizationHeader, addBedObj));
    }


    //Update bed cost using bed id, return the updated Bed object
    @PutMapping("/updateBed/{bedId}/{cost}")
    public Bed updateBedDetails(@PathVariable String bedId, @PathVariable Double cost) {
        return bedService.updateBedDetails(bedId, cost);
    }


    //Get all bed details, returns a list of Bed objects
    @GetMapping("/getAllBeds")
    public List<Bed> getAllBeds() {
        return bedService.getAllBeds();
    }


    //Get beds of a particular hospital using hospital id, returns a list of Bed objects
    @GetMapping("/getByHospital/{hospitalId}")
    public List<Bed> getBedsByHospitalId(@PathVariable String hospitalId) {
        return bedService.getBedsByHospitalId(hospitalId);
    }


    //Get beds which are within the range of the given pincodes, returns a list of Bed objects
    @GetMapping("/getNearByBeds/{pincode}")
    public ResponseEntity<List<Bed>> getNearByBeds(@RequestHeader("Authorization") String authorizationHeader, @NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
        return ResponseEntity.ok().body(bedService.getNearByBeds(authorizationHeader, pincode));
    }


    //Get beds by bed type, returns a list of bed objects
    @GetMapping("/getByType/{bedType}")
    public List<Bed> getBedsByType(@NotBlank(message = "Bed type is required")
                                   @Pattern(regexp = "REGULAR_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type")
                                   @PathVariable String bedType) {
        return bedService.getBedsByType(bedType);
    }


    //Change the status of the bed to "BOOKED", returns the updated bed object
    @PutMapping("/bookBed/{bedId}")
    public Bed bookBed(@PathVariable String bedId) throws BedNotFoundException {
        return bedService.bookBed(bedId);
    }


    //Make the bed status as "AVAILABLE", returns the updated bed object
    @PutMapping("/makeBedAvailable/{bedId}")
    public Bed makeBedAvailable(@PathVariable String bedId) throws BedNotFoundException, BedStatusInvalidException {
        return bedService.makeBedAvailable(bedId);
    }


    //Delete a bed from database, returns the deleted bed object
    @DeleteMapping("/deleteBed/{bedId}")
    public Bed deleteBed(@PathVariable String bedId) {
        return bedService.deleteBed(bedId);
    }
}
