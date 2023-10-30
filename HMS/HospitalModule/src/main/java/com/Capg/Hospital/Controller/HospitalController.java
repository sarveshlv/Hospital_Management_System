package com.Capg.Hospital.Controller;

import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.DTO.UpdateHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Service.HospitalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //Register hospital into the database, returns the created hospital object
    @PostMapping("/register")
    public Hospital registerHospital(@Valid @RequestBody AddHospitalDTO hospitalDTO) {
        return hospitalService.registerHospital(hospitalDTO);
    }

    //Get all the hospitals, returns a list of all hospitals
    @GetMapping("/getHospitals")
    public List<Hospital> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    //Update hospital details using hospital ID, returns the updated hospital object
    @PutMapping("/updateHospital/{id}")
    public Hospital updateHospital(@PathVariable String id, @Valid @RequestBody UpdateHospitalDTO updateHospitalDTO) {
        return hospitalService.updateHospital(id, updateHospitalDTO);
    }

    //Change the hospital status to "APPROVED", returns the updated hospital object
    @PutMapping("/verifyRegistration/{hospitalId}")
    public Hospital approveDeclineRegistration(@PathVariable String hospitalId, @RequestParam String status) throws HospitalNotFoundException {
        return hospitalService.verifyHospital(hospitalId, status);
    }

    //Find nearby hospitals wrt pincode provided, returns a list of Hospital objects
    @GetMapping("/findNearbyHospitals/{pincode}")
    public List<Hospital> findNearbyHospitals(@NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
        return hospitalService.findNearbyHospitals(pincode);
    }

    //Get hospital using hospital ID, returns an hospital object
    @GetMapping("/getHospital/{hospitalId}")
    public Hospital getHospitalById(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return hospitalService.findHospitalById(hospitalId);
    }

    //Delete hospital from database, returns the deleted hospital object
    @DeleteMapping("/deleteHospital/{hospitalId}")
    public Hospital deleteHospitalById(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return hospitalService.deleteHospitalById(hospitalId);
    }
}
