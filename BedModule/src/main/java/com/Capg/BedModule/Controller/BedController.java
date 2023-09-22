package com.Capg.BedModule.Controller;

import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Exceptions.BedNotFoundException;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Service.BedService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bed")
public class BedController {

    @Autowired
    private BedService bedService;

    @PostMapping("/register")
    public ResponseEntity<?> registerBed(@Valid @RequestBody AddBedDTO addBedObj) {
        try {
            return new ResponseEntity<>(bedService.registerBed(addBedObj),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getAllBeds")
    public List<Bed> getAllBeds() {
        return bedService.getAllBeds();
    }
    @GetMapping("/getByHospital/{hospitalId}")
    public List<Bed> getBedsByHospitalId(@PathVariable String hospitalId) {
        return bedService.getBedsByHospitalId(hospitalId);
    }

    @GetMapping("/getNearByBeds/{pincode}")
    public List<Bed> getNearByBeds(@NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
        return bedService.getNearByBeds(pincode);
    }

    @GetMapping("/getByType/{bedType}")
    public List<Bed> getBedsByType(@NotBlank(message = "Bed type is required")
                                   @Pattern(regexp = "REGULAR_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type")
                                   @PathVariable String bedType) {
        return bedService.getBedsByType(bedType);
    }

    @PostMapping("/bookBed/{bedId}")
    public Bed bookBed(@PathVariable String bedId) throws BedNotFoundException {
        return bedService.bookBed(bedId);
    }

    @PostMapping("/makeBedAvailable/{bedId}")
    public Bed makeBedAvailable(@PathVariable String bedId) throws BedNotFoundException {
        return bedService.makeBedAvailable(bedId);
    }
}
