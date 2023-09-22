package com.Capg.Hospital.Controller;

import com.Capg.Hospital.DTO.AddHospitalDTO;
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
    public ResponseEntity<?> registerHospital(@Valid @RequestBody AddHospitalDTO hospitalDTO) {

        try {
            hospitalService.registerHospital(hospitalDTO);
            return new ResponseEntity<>("Hospital Registered Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Access to only Admin
    @PutMapping("/verifyRegistration/{hospitalId}")
    public ResponseEntity<?> approveDeclineRegistration(@PathVariable String hospitalId, @RequestParam String status) {
        try {
            hospitalService.verifyHospital(hospitalId, status);
            return new ResponseEntity<>("Hospital Verified Successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Access to users
    @GetMapping("/findNearbyHospitals/{pincode}")
    public List<Hospital> findNearbyHospitals(@NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
        return hospitalService.findNearbyHospitals(pincode);
    }

    //Access to manager, user
    @GetMapping("/getHospital/{hospitalId}")
    public ResponseEntity<?> getHospitalById(@PathVariable String hospitalId) {
        try {
            return new ResponseEntity<>(hospitalService.findHospitalById(hospitalId), HttpStatus.OK);
        } catch (Exception e) {
            //Provide specific action ahead
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteHospital/{hospitalId}")
    public Hospital deleteHospitalById(@PathVariable String hospitalId) {
        try {
            return hospitalService.deleteHospitalById(hospitalId);
        } catch (Exception e) {
            //Provide specific action ahead
            return null;
        }
    }



}
