package com.hms.hospitalms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.hospitalms.dto.AddHospitalRequest;
import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.service.IHospitalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

	@Autowired
	private IHospitalService hospitalService;

	@PostMapping("/add")
	public Hospital addHospital(@Valid @RequestBody AddHospitalRequest addHospitalRequest) {
		return hospitalService.addHospital(addHospitalRequest);
	}

	@PutMapping("/update/{id}")
	public Hospital updateHospital(@PathVariable String id, @Valid @RequestBody AddHospitalRequest addHospitalRequest) {
		return hospitalService.updateHospital(id, addHospitalRequest);
	}

	@GetMapping("/findById/{id}")
	public Hospital getHospitalById(@PathVariable String id) {
		return hospitalService.findHospitalById(id);
	}
	
	@GetMapping("/getAllHospitals")
	public List<Hospital> getAllHospitals(){
		return hospitalService.getAllHositals();
	}
	@GetMapping("/isVerified/{id}")
	public Boolean isHospitalVerified(@PathVariable String id) {
		return hospitalService.isHospitalVerified(id);
	}

	@PutMapping("/verify/{id}")
	public Hospital verifyHospital(@PathVariable String id) {
		return hospitalService.verifyHospital(id);
	}

	@GetMapping("/nearby/{pincode}")
	public List<Hospital> getNearbyHospitals(
			@PathVariable Long pincode) {
		return hospitalService.getNearbyHospitals(pincode);
	}
}