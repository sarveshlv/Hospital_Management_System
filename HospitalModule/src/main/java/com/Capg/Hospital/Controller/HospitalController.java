package com.Capg.Hospital.Controller;

import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Service.HospitalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //Access to only Manager of Hospital
    @PostMapping("/register")
    public Hospital registerHospital(@Valid @RequestBody AddHospitalDTO hospitalDTO) {
        return hospitalService.registerHospital(hospitalDTO);
    }

    @PutMapping("/updateHospital/{id}")
    public Hospital updateHospital(@PathVariable String id, @Valid @RequestBody AddHospitalDTO addHospitalDTO) {
        return hospitalService.updateHospital(id, addHospitalDTO);
    }

    //Access to only Admin
    @PutMapping("/verifyRegistration/{hospitalId}")
    public Hospital approveDeclineRegistration(@PathVariable String hospitalId, @RequestParam String status) throws HospitalNotFoundException {
        return hospitalService.verifyHospital(hospitalId, status);
    }

    //Access to users
    @GetMapping("/findNearbyHospitals/{pincode}")
    public List<Hospital> findNearbyHospitals(@NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
        return hospitalService.findNearbyHospitals(pincode);
    }

    //Access to manager, user
    @GetMapping("/getHospital/{hospitalId}")
    public Hospital getHospitalById(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return hospitalService.findHospitalById(hospitalId);
    }

    @DeleteMapping("/deleteHospital/{hospitalId}")
    public Hospital deleteHospitalById(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return hospitalService.deleteHospitalById(hospitalId);
    }
}
